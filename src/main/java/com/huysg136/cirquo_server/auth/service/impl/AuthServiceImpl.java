package com.huysg136.cirquo_server.auth.service.impl;

import com.huysg136.cirquo_server.auth.dto.request.RegisterRequest;
import com.huysg136.cirquo_server.auth.service.AuthService;
import com.huysg136.cirquo_server.user.dto.response.UserResponse;
import com.huysg136.cirquo_server.user.entity.Role;
import com.huysg136.cirquo_server.user.entity.User;
import com.huysg136.cirquo_server.user.enums.RoleName;
import com.huysg136.cirquo_server.user.enums.UserStatus;
import com.huysg136.cirquo_server.user.exception.EmailAlreadyExistsException;
import com.huysg136.cirquo_server.user.mapper.UserMapper;
import com.huysg136.cirquo_server.user.repository.RoleRepository;
import com.huysg136.cirquo_server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException();
        }

        Role customerRole = roleRepository.findByName(RoleName.CUSTOMER)
                .orElseThrow(() -> new IllegalStateException(
                        "Default CUSTOMER role is not configured"
                ));

        User user = userMapper.toEntity(request);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setStatus(UserStatus.ACTIVE);
        user.setRole(customerRole);

        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }
}
