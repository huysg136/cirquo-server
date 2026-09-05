package com.huysg136.cirquo_server.catalog.dto.request;

import com.huysg136.cirquo_server.catalog.enums.CatalogStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CategoryRequest(
        UUID parentId,

        @NotBlank(message = "Category name is required!")
        @Size(max = 100, message = "Category name must not exceed 100 characters!")
        String name,

        @NotBlank(message = "Category slug is required!")
        @Size(max = 150, message = "Category slug must not exceed 150 characters!")
        @Pattern(
                regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
                message = "Slug must use lowercase letters, numbers and hyphens only!"
        )
        String slug,

        CatalogStatus status
) {
}
