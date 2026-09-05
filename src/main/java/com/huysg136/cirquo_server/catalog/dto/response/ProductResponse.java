package com.huysg136.cirquo_server.catalog.dto.response;

import com.huysg136.cirquo_server.catalog.enums.CatalogStatus;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        UUID categoryId,
        String categoryName,
        String name,
        String slug,
        String shortDescription,
        String description,
        Map<String, String> specs,
        CatalogStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}