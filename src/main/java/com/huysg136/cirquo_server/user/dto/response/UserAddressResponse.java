package com.huysg136.cirquo_server.user.dto.response;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserAddressResponse(
        UUID id,
        String recipientName,
        String phone,
        String province,
        String ward,
        String addressLine,
        boolean defaultAddress,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
