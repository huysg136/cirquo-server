package com.huysg136.cirquo_server.catalog.controller;

import com.huysg136.cirquo_server.catalog.dto.request.ProductRequest;
import com.huysg136.cirquo_server.catalog.dto.response.ProductCursorResponse;
import com.huysg136.cirquo_server.catalog.dto.response.ProductResponse;
import com.huysg136.cirquo_server.catalog.service.ProductService;
import com.huysg136.cirquo_server.common.ApiResponse;
import com.huysg136.cirquo_server.common.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(
        name = "Products",
        description = "Manage product catalog"
)
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController extends BaseController {

    private final ProductService productService;

    @Operation(
            summary = "Create a product",
            description = "Creates a product in the selected category."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductRequest productRequest
    ) {
        return success(
                HttpStatus.CREATED,
                "Product created successfully!",
                productService.createProduct(productRequest)
        );
    }

    @Operation(
            summary = "Get all active products",
            description = "Returns active products using cursor pagination."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<ProductCursorResponse>> getActiveProducts(
            @RequestParam(required = false) String cursor,

            @RequestParam(defaultValue = "12")
            @Min(value = 1, message = "Size must be greater than 0!")
            @Max(value = 100, message = "Size must not exceed 100!")
            int size
    ) {
        return success(
                HttpStatus.OK,
                "Products retrieved successfully!",
                productService.getActiveProducts(cursor, size)
        );
    }

    @Operation(
            summary = "Get active product by slug",
            description = "Returns an active product by its URL slug."
    )
    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<ProductResponse>> getActiveProductBySlug(
            @PathVariable String slug
    ) {
        return success(
                HttpStatus.OK,
                "Product retrieved successfully!",
                productService.getActiveProductBySlug(slug)
        );
    }

    @Operation(
            summary = "Update a product",
            description = "Updates product information, category and status."
    )
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable UUID productId,
            @Valid @RequestBody ProductRequest productRequest
    ) {
        return success(
                HttpStatus.OK,
                "Product updated successfully!",
                productService.updateProduct(productId, productRequest)
        );
    }
}