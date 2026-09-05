package com.huysg136.cirquo_server.catalog.service;

import com.huysg136.cirquo_server.catalog.dto.request.ProductRequest;
import com.huysg136.cirquo_server.catalog.dto.response.ProductCursorResponse;
import com.huysg136.cirquo_server.catalog.dto.response.ProductResponse;

import java.util.UUID;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    ProductCursorResponse getActiveProducts(String cursor, int size);

    ProductResponse getActiveProductBySlug(String slug);

    ProductResponse updateProduct(UUID productId, ProductRequest request);

}