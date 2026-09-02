package com.huysg136.cirquo_server.auth.exception;

import com.huysg136.cirquo_server.exception.AppException;
import com.huysg136.cirquo_server.exception.ErrorCode;

public class UserNotActiveException extends AppException {
    public UserNotActiveException () {
        super(ErrorCode.USER_NOT_ACTIVE);
    }
}
