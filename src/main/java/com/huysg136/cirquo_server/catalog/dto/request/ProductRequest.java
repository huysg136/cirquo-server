package com.huysg136.cirquo_server.catalog.dto.request;

import com.huysg136.cirquo_server.catalog.enums.CatalogStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Map;
import java.util.UUID;

public record ProductRequest(
        @NotNull(message = "Category is required!")
        UUID categoryId,

        @NotBlank(message = "Product name is required!")
        @Size(max = 255, message = "Product name must not exceed 255 characters!")
        String name,

        @NotBlank(message = "Product slug is required!")
        @Size(max = 255, message = "Product slug must not exceed 255 characters!")
        @Pattern(
                regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
                message = "Slug must use lowercase letters, numbers and hyphens only!"
        )
        String slug,

        String shortDescription,

        String description,

        Map<String, String> specs,

        CatalogStatus status
) {
}