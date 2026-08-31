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
            "Validation failed"
    ),

    BAD_REQUEST(
            1002,
            HttpStatus.BAD_REQUEST,
            "Bad request"
    ),

    UNCATEGORIZED_ERROR(
            9999,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Unexpected error"
    );

    private final int code;
    private final HttpStatus httpStatus;
    private final String message;
}
