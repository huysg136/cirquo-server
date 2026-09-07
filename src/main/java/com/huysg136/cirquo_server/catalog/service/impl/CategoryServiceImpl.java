package com.huysg136.cirquo_server.catalog.service.impl;

import com.huysg136.cirquo_server.catalog.dto.request.CategoryCreateRequest;
import com.huysg136.cirquo_server.catalog.dto.request.CategoryUpdateRequest;
import com.huysg136.cirquo_server.catalog.dto.response.CategoryResponse;
import com.huysg136.cirquo_server.catalog.entity.Category;
import com.huysg136.cirquo_server.catalog.enums.CatalogStatus;
import com.huysg136.cirquo_server.catalog.exception.CategoryNotFoundException;
import com.huysg136.cirquo_server.catalog.exception.CategorySlugAlreadyExistsException;
import com.huysg136.cirquo_server.catalog.mapper.CategoryMapper;
import com.huysg136.cirquo_server.catalog.repository.CategoryRepository;
import com.huysg136.cirquo_server.catalog.service.CategoryService;
import com.huysg136.cirquo_server.exception.AppException;
import com.huysg136.cirquo_server.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    @Override
    public CategoryResponse createCategory(CategoryCreateRequest request) {
        if (categoryRepository.existsBySlug(request.slug())) {
            throw new CategorySlugAlreadyExistsException();
        }

        Category category = categoryMapper.toEntity(request);
        category.setParent(findParent(request.parentId()));
        category.setStatus(
                request.status() == null
                        ? CatalogStatus.ACTIVE
                        : request.status()
        );

        return categoryMapper.toResponse(
                categoryRepository.save(category)
        );
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryResponse> getAllActiveCategories() {
        return categoryRepository
                .findAllByStatusOrderByNameAsc(CatalogStatus.ACTIVE)
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryResponse getActiveCategoryBySlug(String slug) {
        Category category = categoryRepository
                .findBySlugAndStatus(slug, CatalogStatus.ACTIVE)
                .orElseThrow(CategoryNotFoundException::new);

        return categoryMapper.toResponse(category);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryResponse> getCategoriesForAdmin(
            CatalogStatus status,
            String keyword
    ) {
        return categoryRepository
                .findForAdmin(status, normalizeKeyword(keyword))
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryResponse getCategoryById(UUID categoryId) {
        return categoryMapper.toResponse(findCategory(categoryId));
    }

    @Transactional
    @Override
    public CategoryResponse updateCategory(UUID id, CategoryUpdateRequest request) {
        Category category = findCategory(id);

        if (categoryRepository.existsBySlugAndIdNot(request.slug(), id)) {
            throw new CategorySlugAlreadyExistsException();
        }

        if (id.equals(request.parentId())) {
            throw new AppException(ErrorCode.BAD_REQUEST);
        }

        categoryMapper.updateEntity(request, category);
        category.setParent(findParent(request.parentId()));

        return categoryMapper.toResponse(category);
    }

    @Transactional
    @Override
    public void changeStatus(UUID categoryId, CatalogStatus status) {
        Category category = findCategory(categoryId);
        category.setStatus(status);
    }

    private Category findCategory(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(CategoryNotFoundException::new);
    }

    private Category findParent(UUID parentId) {
        if (parentId == null) {
            return null;
        }

        return categoryRepository.findById(parentId)
                .orElseThrow(CategoryNotFoundException::new);
    }

    private String normalizeKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        return keyword.trim();
    }
}
