package com.huysg136.cirquo_server.user;

import com.huysg136.cirquo_server.user.dto.CreateUserRequest;
import com.huysg136.cirquo_server.user.dto.UpdateUserRequest;
import com.huysg136.cirquo_server.user.dto.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    void create(CreateUserRequest createUserRequest);

    List<UserResponse> read();

    void update(UUID id, UpdateUserRequest updateUserRequest);

    void delete(UUID id);
}
