package com.huysg136.cirquo_server.user.service;

import com.huysg136.cirquo_server.common.PageResponse;
import com.huysg136.cirquo_server.user.dto.request.UpdateUserRequest;
import com.huysg136.cirquo_server.user.dto.response.UserResponse;
import com.huysg136.cirquo_server.user.enums.RoleName;
import com.huysg136.cirquo_server.user.enums.UserStatus;

import java.util.UUID;

public interface UserService {
    PageResponse<UserResponse> getUsersForAdmin(
            UserStatus status,
            RoleName roleName,
            String keyword,
            int page,
            int size
    );

    UserResponse getUserById(UUID userId);

    UserResponse updateUser(UUID userId, UpdateUserRequest request);

    void changeStatus(UUID userId, UserStatus status);

    UserResponse changeRole(UUID userId, RoleName roleName);
}
