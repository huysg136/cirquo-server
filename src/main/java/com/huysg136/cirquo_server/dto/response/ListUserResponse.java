package com.huysg136.cirquo_server.dto.response;

import com.huysg136.cirquo_server.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class ListUserResponse {
    private UUID id;
    private String email;
    private String fullName;
    private String phone;
    private UserStatus status = UserStatus.ACTIVE;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
