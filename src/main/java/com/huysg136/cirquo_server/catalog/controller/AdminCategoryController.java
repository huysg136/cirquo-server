package com.huysg136.cirquo_server.catalog.controller;

import com.huysg136.cirquo_server.catalog.dto.request.CategoryCreateRequest;
import com.huysg136.cirquo_server.catalog.dto.request.CategoryUpdateRequest;
import com.huysg136.cirquo_server.catalog.dto.request.ChangeCatalogStatusRequest;
import com.huysg136.cirquo_server.catalog.dto.response.CategoryResponse;
import com.huysg136.cirquo_server.catalog.enums.CatalogStatus;
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
        name = "Admin Categories",
        description = "Manage product categories"
)
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
@RestController
@RequestMapping("/api/v1/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController extends BaseController {

    private final CategoryService categoryService;

    @Operation(
            summary = "Get categories for administration",
            description = "Returns active and inactive categories with optional filters."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategories(
            @RequestParam(required = false) CatalogStatus status,
            @RequestParam(required = false) String keyword
    ) {
        return success(
                HttpStatus.OK,
                "Categories retrieved successfully!",
                categoryService.getCategoriesForAdmin(status, keyword)
        );
    }

    @Operation(
            summary = "Get a category by ID",
            description = "Returns a category for administration."
    )
    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(
            @PathVariable UUID categoryId
    ) {
        return success(
                HttpStatus.OK,
                "Category retrieved successfully!",
                categoryService.getCategoryById(categoryId)
        );
    }

    @Operation(
            summary = "Create a category",
            description = "Creates a product category. The parent category is optional."
    )
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryCreateRequest request
    ) {
        return success(
                HttpStatus.CREATED,
                "Category created successfully!",
                categoryService.createCategory(request)
        );
    }

    @Operation(
            summary = "Update a category",
            description = "Updates category information without changing its status."
    )
    @PutMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable UUID categoryId,
            @Valid @RequestBody CategoryUpdateRequest request
    ) {
        return success(
                HttpStatus.OK,
                "Category updated successfully!",
                categoryService.updateCategory(categoryId, request)
        );
    }

    @Operation(
            summary = "Change category status",
            description = "Changes the category status without deleting it."
    )
    @PatchMapping("/{categoryId}/status")
    public ResponseEntity<ApiResponse<Void>> changeStatus(
            @PathVariable UUID categoryId,
            @Valid @RequestBody ChangeCatalogStatusRequest request
    ) {
        categoryService.changeStatus(categoryId, request.status());

        return success(
                HttpStatus.OK,
                "Category status changed successfully!",
                null
        );
    }
}
