package com.huysg136.cirquo_server.catalog.controller;

import com.huysg136.cirquo_server.catalog.dto.request.CategoryRequest;
import com.huysg136.cirquo_server.catalog.dto.response.CategoryResponse;
import com.huysg136.cirquo_server.catalog.service.CategoryService;
import com.huysg136.cirquo_server.common.ApiResponse;
import com.huysg136.cirquo_server.common.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(
        name = "Categories",
        description = "Manage product categories"
)
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController extends BaseController {

    private final CategoryService categoryService;

    @Operation(
            summary = "Create a category",
            description = "Creates a product category. The parent category is optional."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryRequest categoryRequest
    ) {
        return success(
                HttpStatus.CREATED,
                "Category created successfully!",
                categoryService.createCategory(categoryRequest)
        );
    }

    @Operation(
            summary = "Get active categories",
            description = "Returns all active product categories."
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllActiveCategories() {
        return success(
                HttpStatus.OK,
                "Categories retrieved successfully!",
                categoryService.getAllActiveCategories()
        );
    }

    @Operation(
            summary = "Update a category",
            description = "Updates a product category."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PutMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable UUID categoryId,
            @Valid @RequestBody CategoryRequest categoryRequest
    ) {
        return success(
                HttpStatus.OK,
                "Category updated successfully!",
                categoryService.updateCategory(categoryId, categoryRequest)
        );
    }

    @Operation(
            summary = "Deactivate a category",
            description = "Changes the category status to INACTIVE instead of deleting it permanently."
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(
            @PathVariable UUID categoryId
    ) {
        categoryService.deleteCategory(categoryId);

        return success(
                HttpStatus.OK,
                "Category deactivated successfully!",
                null
        );
    }
}