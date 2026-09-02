package com.huysg136.cirquo_server.user.dto.request;

import com.huysg136.cirquo_server.user.enums.UserStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeUserStatusRequest(
        @NotNull(message = "User status is required!")
        UserStatus status
) {
}
