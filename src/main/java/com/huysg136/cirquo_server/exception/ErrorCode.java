package com.huysg136.cirquo_server.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common: 1000 - 1099
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

    UNCATEGORIZED_ERROR(
            1099,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Unexpected error!"
    ),

    // Authentication: 1100 - 1199
    INVALID_CREDENTIALS(
            1101,
            HttpStatus.UNAUTHORIZED,
            "Email or password is incorrect!"
    ),

    INVALID_REFRESH_TOKEN(
            1102,
            HttpStatus.UNAUTHORIZED,
            "Refresh token is invalid or expired!"
    ),

    NEW_PASSWORD_SAME_AS_CURRENT(
            1103,
            HttpStatus.BAD_REQUEST,
            "New password must be different from current password!"
    ),

    PASSWORD_CONFIRMATION_MISMATCH(
            1104,
            HttpStatus.BAD_REQUEST,
            "New password and confirmation password do not match!"
    ),

    CURRENT_PASSWORD_INCORRECT(
            1105,
            HttpStatus.BAD_REQUEST,
            "Current password is incorrect!"
    ),

    UNAUTHENTICATED(
            1106,
            HttpStatus.UNAUTHORIZED,
            "Authentication is required!"
    ),

    ACCESS_DENIED(
            1107,
            HttpStatus.FORBIDDEN,
            "You do not have permission to perform this action!"
    ),

    // User: 1200 - 1299
    EMAIL_ALREADY_EXISTS(
            1201,
            HttpStatus.CONFLICT,
            "Email already exists!"
    ),

    USER_NOT_FOUND(
            1202,
            HttpStatus.NOT_FOUND,
            "User not found!"
    ),

    USER_NOT_ACTIVE(
            1203,
            HttpStatus.FORBIDDEN,
            "User account is not active!"
    ),

    USER_SUSPENDED(
            1204,
            HttpStatus.FORBIDDEN,
            "User account is suspended!"
    ),

    // User address: 1300 - 1399
    USER_ADDRESS_NOT_FOUND(
            1301,
            HttpStatus.NOT_FOUND,
            "User address not found!"
    ),

    // Catalog: 1400 - 1499
    CATEGORY_NOT_FOUND(
        1401,
        HttpStatus.NOT_FOUND,
        "Category not found!"
    ),

    CATEGORY_SLUG_ALREADY_EXISTS(
        1402,
        HttpStatus.CONFLICT,
        "Category slug already exists!"
    );

    private final int code;
    private final HttpStatus httpStatus;
    private final String message;
}
