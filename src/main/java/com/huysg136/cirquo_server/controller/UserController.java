package com.huysg136.cirquo_server.controller;

import com.huysg136.cirquo_server.dto.request.CreateUserRequest;
import com.huysg136.cirquo_server.dto.request.UpdateUserRequest;
import com.huysg136.cirquo_server.dto.response.ListUserResponse;
import com.huysg136.cirquo_server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody CreateUserRequest createUserRequest){
        userService.create(createUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully!");
    }

    @GetMapping
    public ResponseEntity<List<ListUserResponse>> getAllUsers(){
        List<ListUserResponse> users = userService.read();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable("id") UUID id, @RequestBody UpdateUserRequest updateUserRequest){
        userService.update(id, updateUserRequest);
        return ResponseEntity.status(HttpStatus.OK).body("User updated successfully!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") UUID id){
        userService.delete(id);
        return  ResponseEntity.status(HttpStatus.OK).body("User deleted successfully!");
    }
}
