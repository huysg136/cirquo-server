package com.huysg136.cirquo_server.catalog.controller;

import com.huysg136.cirquo_server.catalog.dto.response.CategoryResponse;
import com.huysg136.cirquo_server.catalog.dto.response.ProductCursorResponse;
import com.huysg136.cirquo_server.catalog.service.CategoryService;
import com.huysg136.cirquo_server.catalog.service.ProductService;
import com.huysg136.cirquo_server.common.ApiResponse;
import com.huysg136.cirquo_server.common.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "Storefront Categories",
        description = "Browse active product categories"
)
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController extends BaseController {

    private final CategoryService categoryService;
    private final ProductService productService;

    @Operation(
            summary = "Get active categories",
            description = "Returns all active product categories."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllActiveCategories() {
        return success(
                HttpStatus.OK,
                "Categories retrieved successfully!",
                categoryService.getAllActiveCategories()
        );
    }

    @Operation(
            summary = "Get an active category by slug",
            description = "Returns an active category for the storefront."
    )
    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getActiveCategoryBySlug(
            @PathVariable String slug
    ) {
        return success(
                HttpStatus.OK,
                "Category retrieved successfully!",
                categoryService.getActiveCategoryBySlug(slug)
        );
    }

    @Operation(
            summary = "Get active products by category",
            description = "Returns active products in an active category using cursor pagination."
    )
    @GetMapping("/{categorySlug}/products")
    public ResponseEntity<ApiResponse<ProductCursorResponse>> getActiveProductsByCategory(
            @PathVariable String categorySlug,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "12")
            @Min(value = 1, message = "Size must be greater than 0!")
            @Max(value = 100, message = "Size must not exceed 100!")
            int size
    ) {
        return success(
                HttpStatus.OK,
                "Products retrieved successfully!",
                productService.getActiveProductsByCategorySlug(
                        categorySlug,
                        cursor,
                        size
                )
        );
    }
}
