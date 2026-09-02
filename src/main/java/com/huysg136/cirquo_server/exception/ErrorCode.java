package com.huysg136.cirquo_server.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    VALIDATION_ERROR(
            1001,
            HttpStatus.BAD_REQUEST,
            "Validation failed!"
    ),

    BAD_REQUEST(
            1002,
            HttpStatus.BAD_REQUEST,
            "Bad request!"
    ),

    EMAIL_ALREADY_EXISTS(
            1003,
            HttpStatus.CONFLICT,
            "Email already exists!"
    ),

    USER_NOT_FOUND(
            1004,
            HttpStatus.NOT_FOUND,
            "User not found!"
    ),

    ADDRESS_NOT_FOUND(
            1005,
            HttpStatus.NOT_FOUND,
            "Address not found!"
    ),

    INVALID_CREDENTIALS(
            1006,
            HttpStatus.UNAUTHORIZED,
            "Email or password is incorrect!"
    ),

    USER_NOT_ACTIVE(
            1007,
            HttpStatus.FORBIDDEN,
            "User account is not active!"
    ),

    INVALID_REFRESH_TOKEN(
            1008,
            HttpStatus.UNAUTHORIZED,
            "Refresh token is invalid or expired!"
    ),

    NEW_PASSWORD_SAME_AS_CURRENT(
            1009,
            HttpStatus.BAD_REQUEST,
            "New password must be different from current password!"
    ),

    PASSWORD_CONFIRMATION_MISMATCH(
            1010,
            HttpStatus.BAD_REQUEST,
            "New password and confirmation password do not match!"
    ),

    CURRENT_PASSWORD_INCORRECT(
            1011,
            HttpStatus.BAD_REQUEST,
            "Current password is incorrect!"
    ),

    UNCATEGORIZED_ERROR(
            9999,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Unexpected error!"
    );

    private final int code;
    private final HttpStatus httpStatus;
    private final String message;
}
