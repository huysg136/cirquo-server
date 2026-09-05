package com.huysg136.cirquo_server.catalog.service;

import com.huysg136.cirquo_server.catalog.dto.request.CategoryRequest;
import com.huysg136.cirquo_server.catalog.dto.response.CategoryResponse;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);

    List<CategoryResponse> getAllActiveCategories();

    CategoryResponse updateCategory(UUID categoryId, CategoryRequest request);

    void deleteCategory(UUID categoryId);
}
