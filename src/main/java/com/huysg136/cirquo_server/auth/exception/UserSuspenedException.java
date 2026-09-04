package com.huysg136.cirquo_server.auth.exception;

import com.huysg136.cirquo_server.exception.AppException;
import com.huysg136.cirquo_server.exception.ErrorCode;

public class UserSuspenedException extends AppException {
    public UserSuspenedException() {
        super(ErrorCode.USER_SUSPENDED);
    }
}
