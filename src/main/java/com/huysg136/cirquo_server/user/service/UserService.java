package com.huysg136.cirquo_server.user.service;

import com.huysg136.cirquo_server.user.dto.request.CreateUserRequest;
import com.huysg136.cirquo_server.user.dto.request.UpdateUserRequest;
import com.huysg136.cirquo_server.user.dto.response.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponse create(CreateUserRequest createUserRequest);

    List<UserResponse> read();

    UserResponse update(UUID userId, UpdateUserRequest updateUserRequest);

    void delete(UUID userId);
}
