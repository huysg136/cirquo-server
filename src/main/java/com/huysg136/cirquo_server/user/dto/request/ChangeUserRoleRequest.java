package com.huysg136.cirquo_server.user.dto.request;

import com.huysg136.cirquo_server.user.enums.RoleName;
import jakarta.validation.constraints.NotNull;

public record ChangeUserRoleRequest(
        @NotNull(message = "Role is required!")
        RoleName roleName
) {
}
