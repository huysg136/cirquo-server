package com.huysg136.cirquo_server.catalog.dto.response;

import com.huysg136.cirquo_server.catalog.enums.CatalogStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CategoryResponse(
        UUID id,
        UUID parentId,
        String name,
        String slug,
        CatalogStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
