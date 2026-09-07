package com.huysg136.cirquo_server.catalog.dto.request;

import com.huysg136.cirquo_server.catalog.enums.CatalogStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeCatalogStatusRequest(
        @NotNull(message = "Catalog status is required!")
        CatalogStatus status
) {
}
