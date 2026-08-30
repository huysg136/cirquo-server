package com.huysg136.cirquo_server.service.impl;

import com.huysg136.cirquo_server.dto.request.CreateUserRequest;
import com.huysg136.cirquo_server.dto.request.UpdateUserRequest;
import com.huysg136.cirquo_server.dto.response.ListUserResponse;
import com.huysg136.cirquo_server.entity.User;
import com.huysg136.cirquo_server.enums.UserStatus;
import com.huysg136.cirquo_server.repository.UserRepository;
import com.huysg136.cirquo_server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IUserService implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void create(CreateUserRequest createUserRequest) {
        userRepository.save(User.builder()
                .email(createUserRequest.getEmail())
                .password(passwordEncoder.encode(createUserRequest.getPassword()))
                .fullName(createUserRequest.getFullName())
                .phone(createUserRequest.getPhone())
                .status(UserStatus.ACTIVE)
                .build()
        );
    }

    @Override
    public List<ListUserResponse> read() {
        List<User> users = userRepository.findAll();
        List<ListUserResponse> responseList = new ArrayList<>();

        for (User user: users){
            ListUserResponse response = ListUserResponse.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .phone(user.getPhone())
                    .status(user.getStatus())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .build();

            responseList.add(response);
        }

        return responseList;
    }

    @Override
    public void update(UUID id, UpdateUserRequest updateUserRequest) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()){
            throw new IllegalArgumentException("User does not exist!");
        }

        User userUpdate = user.get();
        userUpdate.setEmail(updateUserRequest.getEmail());
        userUpdate.setFullName(updateUserRequest.getFullName());
        userUpdate.setPhone(updateUserRequest.getPhone());
        userRepository.save(userUpdate);
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
