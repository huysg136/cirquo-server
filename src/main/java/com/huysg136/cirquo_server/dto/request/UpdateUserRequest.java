package com.huysg136.cirquo_server.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    private String email;
    private String fullName;
    private String phone;
}