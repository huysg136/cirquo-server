package com.huysg136.cirquo_server.catalog.dto.response;

import java.util.List;

public record ProductCursorResponse(
        List<ProductResponse> items,
        String nextCursor,
        boolean hasNext
) {
}