package com.huysg136.cirquo_server.user.impl;

import com.huysg136.cirquo_server.user.User;
import com.huysg136.cirquo_server.user.UserMapper;
import com.huysg136.cirquo_server.user.UserRepository;
import com.huysg136.cirquo_server.user.UserService;
import com.huysg136.cirquo_server.user.UserStatus;
import com.huysg136.cirquo_server.user.dto.request.CreateUserRequest;
import com.huysg136.cirquo_server.user.dto.request.UpdateUserRequest;
import com.huysg136.cirquo_server.user.dto.response.ListUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public void create(CreateUserRequest createUserRequest) {
        User user = userMapper.toEntity(createUserRequest);
        user.setPassword(passwordEncoder.encode(createUserRequest.password()));
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
    }

    @Override
    public List<ListUserResponse> read() {
        return userRepository.findAll().stream().map(userMapper::toResponse).toList();
    }

    @Override
    public void update(UUID id, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User does not exist!"));

        userMapper.updateEntity(updateUserRequest, user);
        userRepository.save(user);
    }

    @Override
    public void delete(UUID id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()){
            throw new IllegalArgumentException("User does not exist!");
        }

        userRepository.deleteById(id);
    }
}
