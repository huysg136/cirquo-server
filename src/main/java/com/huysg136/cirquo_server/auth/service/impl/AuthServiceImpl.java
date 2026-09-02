package com.huysg136.cirquo_server.auth.service.impl;

import com.huysg136.cirquo_server.auth.dto.request.ChangePasswordRequest;
import com.huysg136.cirquo_server.auth.dto.request.LoginRequest;
import com.huysg136.cirquo_server.auth.dto.request.RefreshTokenRequest;
import com.huysg136.cirquo_server.auth.dto.request.RegisterRequest;
import com.huysg136.cirquo_server.auth.dto.response.AuthResponse;
import com.huysg136.cirquo_server.auth.exception.*;
import com.huysg136.cirquo_server.auth.service.AuthService;
import com.huysg136.cirquo_server.auth.service.JwtService;
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
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtService jwtService;

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

    @Transactional(readOnly = true)
    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())){
            throw new InvalidCredentialsException();
        }

        if (user.getStatus() != UserStatus.ACTIVE){
            throw new UserNotActiveException();
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new AuthResponse(
                accessToken,
                refreshToken,
                "Bearer",
                userMapper.toResponse(user)
        );
    }

    @Transactional(readOnly = true)
    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.refreshToken();

        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new InvalidRefreshTokenException();
        }

        UUID userId = jwtService.extractUserId(refreshToken);

        User user = userRepository.findById(userId)
                .orElseThrow(InvalidRefreshTokenException::new);

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new UserNotActiveException();
        }

        String newAccessToken = jwtService.generateAccessToken(user);

        return new AuthResponse(
                newAccessToken,
                refreshToken,
                "Bearer",
                userMapper.toResponse(user)
        );
    }

    @Transactional
    @Override
    public void changePassword(UUID userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(
                request.currentPassword(),
                user.getPasswordHash()
        )) {
            throw new InvalidCurrentPasswordException();
        }

        if (!request.newPassword().equals(request.confirmNewPassword())) {
            throw new PasswordConfirmationMismatchException();
        }

        if (passwordEncoder.matches(
                request.newPassword(),
                user.getPasswordHash()
        )) {
            throw new NewPasswordSameAsCurrentException();
        }

        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }
}
