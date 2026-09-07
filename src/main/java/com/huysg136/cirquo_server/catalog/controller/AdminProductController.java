package com.huysg136.cirquo_server.catalog.controller;

import com.huysg136.cirquo_server.catalog.dto.request.ChangeCatalogStatusRequest;
import com.huysg136.cirquo_server.catalog.dto.request.ProductCreateRequest;
import com.huysg136.cirquo_server.catalog.dto.request.ProductUpdateRequest;
import com.huysg136.cirquo_server.catalog.dto.response.ProductResponse;
import com.huysg136.cirquo_server.catalog.enums.CatalogStatus;
import com.huysg136.cirquo_server.catalog.service.ProductService;
import com.huysg136.cirquo_server.common.ApiResponse;
import com.huysg136.cirquo_server.common.BaseController;
import com.huysg136.cirquo_server.common.PageResponse;
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
        name = "Admin Products",
        description = "Manage product catalog"
)
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
@RestController
@RequestMapping("/api/v1/admin/products")
@RequiredArgsConstructor
public class AdminProductController extends BaseController {

    private final ProductService productService;

    @Operation(
            summary = "Get products for administration",
            description = "Returns paginated products with optional filters."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getProducts(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) CatalogStatus status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page must not be negative!")
            int page,
            @RequestParam(defaultValue = "20")
            @Min(value = 1, message = "Size must be greater than 0!")
            @Max(value = 100, message = "Size must not exceed 100!")
            int size
    ) {
        return success(
                HttpStatus.OK,
                "Products retrieved successfully!",
                productService.getProductsForAdmin(
                        categoryId,
                        status,
                        keyword,
                        page,
                        size
                )
        );
    }

    @Operation(
            summary = "Get a product by ID",
            description = "Returns a product for administration."
    )
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(
            @PathVariable UUID productId
    ) {
        return success(
                HttpStatus.OK,
                "Product retrieved successfully!",
                productService.getProductById(productId)
        );
    }

    @Operation(
            summary = "Create a product",
            description = "Creates a product in the selected category."
    )
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductCreateRequest request
    ) {
        return success(
                HttpStatus.CREATED,
                "Product created successfully!",
                productService.createProduct(request)
        );
    }

    @Operation(
            summary = "Update a product",
            description = "Updates product information without changing its status."
    )
    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable UUID productId,
            @Valid @RequestBody ProductUpdateRequest request
    ) {
        return success(
                HttpStatus.OK,
                "Product updated successfully!",
                productService.updateProduct(productId, request)
        );
    }

    @Operation(
            summary = "Change product status",
            description = "Changes the product status without deleting it."
    )
    @PatchMapping("/{productId}/status")
    public ResponseEntity<ApiResponse<Void>> changeStatus(
            @PathVariable UUID productId,
            @Valid @RequestBody ChangeCatalogStatusRequest request
    ) {
        productService.changeStatus(productId, request.status());

        return success(
                HttpStatus.OK,
                "Product status changed successfully!",
                null
        );
    }
}
