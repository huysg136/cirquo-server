package com.huysg136.cirquo_server.user.exception;

import com.huysg136.cirquo_server.exception.AppException;
import com.huysg136.cirquo_server.exception.ErrorCode;

public class UserNotFoundException extends AppException {

    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}