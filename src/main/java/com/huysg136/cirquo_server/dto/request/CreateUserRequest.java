package com.huysg136.cirquo_server.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    private String email;
    private String password;
    private String fullName;
    private String phone;
}
