package com.huysg136.cirquo_server.user.service.impl;

import com.huysg136.cirquo_server.user.dto.response.UserResponse;
import com.huysg136.cirquo_server.user.entity.Role;
import com.huysg136.cirquo_server.user.entity.User;
import com.huysg136.cirquo_server.user.enums.RoleName;
import com.huysg136.cirquo_server.user.exception.EmailAlreadyExistsException;
import com.huysg136.cirquo_server.user.exception.UserNotFoundException;
import com.huysg136.cirquo_server.user.mapper.UserMapper;
import com.huysg136.cirquo_server.user.repository.RoleRepository;
import com.huysg136.cirquo_server.user.repository.UserRepository;
import com.huysg136.cirquo_server.user.service.UserService;
import com.huysg136.cirquo_server.user.enums.UserStatus;
import com.huysg136.cirquo_server.user.dto.request.CreateUserRequest;
import com.huysg136.cirquo_server.user.dto.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public void create(CreateUserRequest createUserRequest) {
        if (userRepository.existsByEmail(createUserRequest.email())) {
            throw new EmailAlreadyExistsException();
        }

        Role customerRole = roleRepository.findByName(RoleName.CUSTOMER)
                .orElseThrow(() -> new IllegalStateException(
                        "Default CUSTOMER role is not configured"
                ));

        User user = userMapper.toEntity(createUserRequest);
        user.setPasswordHash(passwordEncoder.encode(createUserRequest.password()));
        user.setStatus(UserStatus.ACTIVE);
        user.setRole(customerRole);

        userRepository.save(user);
    }

    @Override
    public List<UserResponse> read() {
        return userRepository.findAll().stream().map(userMapper::toResponse).toList();
    }

    @Override
    public void update(UUID id, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        if (!user.getEmail().equals(updateUserRequest.email())
                && userRepository.existsByEmail(updateUserRequest.email())) {
            throw new EmailAlreadyExistsException();
        }

        userMapper.updateEntity(updateUserRequest, user);
        userRepository.save(user);
    }

    @Override
    public void delete(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        userRepository.delete(user);
    }
}
