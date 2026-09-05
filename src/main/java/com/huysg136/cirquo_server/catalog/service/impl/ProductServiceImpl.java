package com.huysg136.cirquo_server.catalog.service.impl;

import com.huysg136.cirquo_server.catalog.dto.request.ProductRequest;
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
    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.existsBySlug(request.slug())){
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
    public ProductCursorResponse getActiveProducts(
            String cursor,
            int size
    ) {
        PageRequest pageable = PageRequest.of(
                0,
                size + 1
        );

        List<Product> products;

        if (cursor == null || cursor.isBlank()) {
            products = productRepository.findByStatusOrderByCreatedAtDescIdDesc(
                    CatalogStatus.ACTIVE,
                    pageable
            );
        } else {
            ProductCursor productCursor = decodeCursor(cursor);

            products = productRepository.findByStatusAfterCursor(
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
                .findBySlugAndStatus(slug, CatalogStatus.ACTIVE)
                .orElseThrow(ProductNotFoundException::new);

        return productMapper.toResponse(product);
    }

    @Transactional
    @Override
    public ProductResponse updateProduct(UUID productId, ProductRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        if (productRepository.existsBySlugAndIdNot(
                request.slug(),
                productId
        )) {
            throw new ProductSlugAlreadyExistsException();
        }

        CatalogStatus currentStatus = product.getStatus();

        productMapper.updateEntity(request, product);
        product.setCategory(findCategory(request.categoryId()));

        if (request.status() == null) {
            product.setStatus(currentStatus);
        }

        return productMapper.toResponse(product);
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

    private record ProductCursor(
            OffsetDateTime createdAt,
            UUID id
    ) {
    }
}
