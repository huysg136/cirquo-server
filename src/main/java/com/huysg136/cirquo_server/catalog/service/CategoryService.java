package com.huysg136.cirquo_server.catalog.service;

import com.huysg136.cirquo_server.catalog.dto.request.CategoryCreateRequest;
import com.huysg136.cirquo_server.catalog.dto.request.CategoryUpdateRequest;
import com.huysg136.cirquo_server.catalog.dto.response.CategoryResponse;
import com.huysg136.cirquo_server.catalog.enums.CatalogStatus;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    CategoryResponse createCategory(CategoryCreateRequest request);

    List<CategoryResponse> getAllActiveCategories();

    CategoryResponse getActiveCategoryBySlug(String slug);

    List<CategoryResponse> getCategoriesForAdmin(CatalogStatus status, String keyword);

    CategoryResponse getCategoryById(UUID categoryId);

    CategoryResponse updateCategory(UUID categoryId, CategoryUpdateRequest request);

    void changeStatus(UUID categoryId, CatalogStatus status);
}
