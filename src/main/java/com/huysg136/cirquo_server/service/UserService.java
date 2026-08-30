package com.huysg136.cirquo_server.service;

import com.huysg136.cirquo_server.dto.request.CreateUserRequest;
import com.huysg136.cirquo_server.dto.request.UpdateUserRequest;
import com.huysg136.cirquo_server.dto.response.ListUserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    void create(CreateUserRequest createUserRequest);

    List<ListUserResponse> read();

    void update(UUID id, UpdateUserRequest updateUserRequest);

    void delete(UUID id);
}
