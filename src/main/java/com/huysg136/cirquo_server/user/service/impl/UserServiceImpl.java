package com.huysg136.cirquo_server.user.service.impl;

import com.huysg136.cirquo_server.user.dto.response.UserResponse;
import com.huysg136.cirquo_server.user.entity.Role;
import com.huysg136.cirquo_server.user.entity.User;
import com.huysg136.cirquo_server.user.enums.RoleName;
import com.huysg136.cirquo_server.user.enums.UserStatus;
import com.huysg136.cirquo_server.user.exception.EmailAlreadyExistsException;
import com.huysg136.cirquo_server.user.exception.UserNotFoundException;
import com.huysg136.cirquo_server.user.mapper.UserMapper;
import com.huysg136.cirquo_server.user.repository.RoleRepository;
import com.huysg136.cirquo_server.user.repository.UserRepository;
import com.huysg136.cirquo_server.user.service.UserService;
import com.huysg136.cirquo_server.user.dto.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponse getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return userMapper.toResponse(user);
    }

    @Transactional
    @Override
    public UserResponse updateUser(UUID userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        if (!user.getEmail().equals(request.email())
                && userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException();
        }

        userMapper.updateEntity(request, user);
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Transactional
    @Override
    public void changeStatus(UUID userId, UserStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        user.setStatus(status);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public UserResponse changeRole(UUID userId, RoleName roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalStateException(
                        "Role is not configured"
                ));

        user.setRole(role);

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }
}
