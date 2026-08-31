package com.huysg136.cirquo_server.common;

public abstract class BaseController {
    protected <T> ApiResponse<T> createSuccessResponse(T data) {
        return ApiResponse.success(data);
    }
}
