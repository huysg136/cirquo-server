package com.huysg136.cirquo_server.user.dto.response;

import com.huysg136.cirquo_server.user.enums.UserStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ListUserResponse(
        UUID id,
        String email,
        String fullName,
        String phone,
        UserStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
