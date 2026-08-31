package com.huysg136.cirquo_server.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.huysg136.cirquo_server.exception.ErrorCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        int code,
        String message,
        T result
) {
    public static <T> ApiResponse<T> error(
            ErrorCode errorCode,
            T result
    ) {
        return new ApiResponse<>(
                errorCode.getCode(),
                errorCode.getMessage(),
                result
        );
    }

    public static <T> ApiResponse<T> success(T result) {
        return new ApiResponse<>(1000, "Successfully!", result);
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode) {
        return error(errorCode, null);
    }
}
