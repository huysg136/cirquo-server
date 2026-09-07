package com.huysg136.cirquo_server.catalog.service.impl;

import com.huysg136.cirquo_server.catalog.dto.request.ProductCreateRequest;
import com.huysg136.cirquo_server.catalog.dto.request.ProductUpdateRequest;
import com.huysg136.cirquo_server.catalog.dto.response.ProductCursorResponse;
import com.huysg136.cirquo_server.catalog.dto.response.ProductResponse;
import com.huysg136.cirquo_server.catalog.entity.Category;
import com.huysg136.cirquo_server.catalog.entity.Product;
import com.huysg136.cirquo_server.catalog.enums.CatalogStatus;
import com.huysg136.cirquo_server.catalog.exception.CategoryNotFoundException;
import com.huysg136.cirquo_server.catalog.exception.ProductNotFoundException;
import com.huysg136.cirquo_server.catalog.exception.ProductSlugAlreadyExistsException;
import com.huysg136.cirquo_server.catalog.mapper.ProductMapper;
import com.huysg136.cirquo_server.catalog.repository.CategoryRepository;
import com.huysg136.cirquo_server.catalog.repository.ProductRepository;
import com.huysg136.cirquo_server.catalog.service.ProductService;
import com.huysg136.cirquo_server.common.PageResponse;
import com.huysg136.cirquo_server.exception.AppException;
import com.huysg136.cirquo_server.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Transactional
    @Override
    public ProductResponse createProduct(ProductCreateRequest request) {
        if (productRepository.existsBySlug(request.slug())) {
            throw new ProductSlugAlreadyExistsException();
        }

        Product product = productMapper.toEntity(request);
        product.setCategory(findCategory(request.categoryId()));
        product.setStatus(request.status() == null ? CatalogStatus.ACTIVE : request.status());

        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    @Transactional(readOnly = true)
    @Override
    public ProductCursorResponse getActiveProductsByCategorySlug(
            String categorySlug,
            String cursor,
            int size
    ) {
        Category category = categoryRepository
                .findBySlugAndStatus(categorySlug, CatalogStatus.ACTIVE)
                .orElseThrow(CategoryNotFoundException::new);

        PageRequest pageable = PageRequest.of(
                0,
                size + 1
        );

        List<Product> products;

        if (cursor == null || cursor.isBlank()) {
            products = productRepository.findByCategoryIdAndStatusOrderByCreatedAtDescIdDesc(
                    category.getId(),
                    CatalogStatus.ACTIVE,
                    pageable
            );
        } else {
            ProductCursor productCursor = decodeCursor(cursor);

            products = productRepository.findByCategoryAndStatusAfterCursor(
                    category.getId(),
                    CatalogStatus.ACTIVE,
                    productCursor.createdAt(),
                    productCursor.id(),
                    pageable
            );
        }

        boolean hasNext = products.size() > size;

        if (hasNext) {
            products = new ArrayList<>(products.subList(0, size));
        }

        String nextCursor = null;

        if (hasNext && !products.isEmpty()) {
            Product lastProduct = products.getLast();

            nextCursor = encodeCursor(
                    lastProduct.getCreatedAt(),
                    lastProduct.getId()
            );
        }

        return new ProductCursorResponse(
                products.stream()
                        .map(productMapper::toResponse)
                        .toList(),
                nextCursor,
                hasNext
        );
    }

    @Transactional(readOnly = true)
    @Override
    public ProductResponse getActiveProductBySlug(String slug) {
        Product product = productRepository
                .findBySlugAndStatusAndCategoryStatus(
                        slug,
                        CatalogStatus.ACTIVE,
                        CatalogStatus.ACTIVE
                )
                .orElseThrow(ProductNotFoundException::new);

        return productMapper.toResponse(product);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<ProductResponse> getProductsForAdmin(
            UUID categoryId,
            CatalogStatus status,
            String keyword,
            int page,
            int size
    ) {
        return PageResponse.from(
                productRepository.findForAdmin(
                        categoryId,
                        status,
                        normalizeKeyword(keyword),
                        PageRequest.of(
                                page,
                                size,
                                Sort.by(
                                        Sort.Order.desc("createdAt"),
                                        Sort.Order.desc("id")
                                )
                        )
                ),
                productMapper::toResponse
        );
    }

    @Transactional(readOnly = true)
    @Override
    public ProductResponse getProductById(UUID productId) {
        return productMapper.toResponse(findProduct(productId));
    }

    @Transactional
    @Override
    public ProductResponse updateProduct(UUID productId, ProductUpdateRequest request) {
        Product product = findProduct(productId);

        if (productRepository.existsBySlugAndIdNot(
                request.slug(),
                productId
        )) {
            throw new ProductSlugAlreadyExistsException();
        }

        productMapper.updateEntity(request, product);
        product.setCategory(findCategory(request.categoryId()));

        return productMapper.toResponse(product);
    }

    @Transactional
    @Override
    public void changeStatus(UUID productId, CatalogStatus status) {
        Product product = findProduct(productId);
        product.setStatus(status);
    }

    private Product findProduct(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
    }

    private Category findCategory(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(CategoryNotFoundException::new);
    }

    private String encodeCursor(
            OffsetDateTime createdAt,
            UUID id
    ) {
        String value = createdAt + "|" + id;

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private ProductCursor decodeCursor(String cursor) {
        try {
            String value = new String(
                    Base64.getUrlDecoder().decode(cursor),
                    StandardCharsets.UTF_8
            );

            String[] parts = value.split("\\|");

            return new ProductCursor(
                    OffsetDateTime.parse(parts[0]),
                    UUID.fromString(parts[1])
            );
        } catch (Exception exception) {
            throw new AppException(ErrorCode.BAD_REQUEST);
        }
    }

    private String normalizeKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        return keyword.trim();
    }

    private record ProductCursor(
            OffsetDateTime createdAt,
            UUID id
    ) {
    }
}
