package com.huysg136.cirquo_server.catalog.controller;

import com.huysg136.cirquo_server.catalog.dto.response.ProductResponse;
import com.huysg136.cirquo_server.catalog.service.ProductService;
import com.huysg136.cirquo_server.common.ApiResponse;
import com.huysg136.cirquo_server.common.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Storefront Products",
        description = "Browse active products"
)
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController extends BaseController {

    private final ProductService productService;

    @Operation(
            summary = "Get an active product by slug",
            description = "Returns an active product for the storefront."
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
}
