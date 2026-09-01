package com.huysg136.cirquo_server.user.dto.response;

import com.huysg136.cirquo_server.user.enums.RoleName;
import com.huysg136.cirquo_server.user.enums.UserStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        String fullName,
        String phone,
        UserStatus status,
        RoleName roleName,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
