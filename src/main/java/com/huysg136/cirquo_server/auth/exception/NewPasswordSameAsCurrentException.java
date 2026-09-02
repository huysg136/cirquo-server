package com.huysg136.cirquo_server.auth.exception;

import com.huysg136.cirquo_server.exception.AppException;
import com.huysg136.cirquo_server.exception.ErrorCode;

public class NewPasswordSameAsCurrentException extends AppException {
    public NewPasswordSameAsCurrentException() {
        super(ErrorCode.NEW_PASSWORD_SAME_AS_CURRENT);
    }
}
