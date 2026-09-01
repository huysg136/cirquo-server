package com.huysg136.cirquo_server.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {
    protected <T> ResponseEntity<ApiResponse<T>> success(
            HttpStatus httpStatus,
            String message,
            T result
    ) {
        return ResponseEntity
                .status(httpStatus)
                .body(ApiResponse.success(message, result));
    }
}
