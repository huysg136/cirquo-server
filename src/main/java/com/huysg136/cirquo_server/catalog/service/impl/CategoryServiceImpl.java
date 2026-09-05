package com.huysg136.cirquo_server.catalog.service.impl;

import com.huysg136.cirquo_server.catalog.dto.request.CategoryRequest;
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
    public CategoryResponse createCategory(CategoryRequest request) {
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

    @Transactional
    @Override
    public CategoryResponse updateCategory(UUID id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);

        if (categoryRepository.existsBySlugAndIdNot(request.slug(), id)) {
            throw new CategorySlugAlreadyExistsException();
        }

        if (id.equals(request.parentId())) {
            throw new AppException(ErrorCode.BAD_REQUEST);
        }

        categoryMapper.updateEntity(request, category);
        category.setParent(findParent(request.parentId()));

        if (request.status() == null) {
            category.setStatus(CatalogStatus.ACTIVE);
        }

        return categoryMapper.toResponse(category);
    }

    @Transactional
    @Override
    public void deleteCategory(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);

        category.setStatus(CatalogStatus.INACTIVE);
    }

    private Category findParent(UUID parentId) {
        if (parentId == null) {
            return null;
        }

        return categoryRepository.findById(parentId)
                .orElseThrow(CategoryNotFoundException::new);
    }
}