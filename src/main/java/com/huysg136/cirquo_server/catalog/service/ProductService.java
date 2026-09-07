package com.huysg136.cirquo_server.catalog.service;

import com.huysg136.cirquo_server.catalog.dto.request.ProductCreateRequest;
import com.huysg136.cirquo_server.catalog.dto.request.ProductUpdateRequest;
import com.huysg136.cirquo_server.catalog.dto.response.ProductCursorResponse;
import com.huysg136.cirquo_server.catalog.dto.response.ProductResponse;
import com.huysg136.cirquo_server.catalog.enums.CatalogStatus;
import com.huysg136.cirquo_server.common.PageResponse;

import java.util.UUID;

public interface ProductService {

    ProductResponse createProduct(ProductCreateRequest request);

    ProductCursorResponse getActiveProductsByCategorySlug(
            String categorySlug,
            String cursor,
            int size
    );

    ProductResponse getActiveProductBySlug(String slug);

    PageResponse<ProductResponse> getProductsForAdmin(
            UUID categoryId,
            CatalogStatus status,
            String keyword,
            int page,
            int size
    );

    ProductResponse getProductById(UUID productId);

    ProductResponse updateProduct(UUID productId, ProductUpdateRequest request);

    void changeStatus(UUID productId, CatalogStatus status);

}
