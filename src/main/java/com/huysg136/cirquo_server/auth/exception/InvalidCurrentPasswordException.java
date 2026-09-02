package com.huysg136.cirquo_server.auth.exception;

import com.huysg136.cirquo_server.exception.AppException;
import com.huysg136.cirquo_server.exception.ErrorCode;

public class InvalidCurrentPasswordException extends AppException {
    public InvalidCurrentPasswordException(){
        super(ErrorCode.CURRENT_PASSWORD_INCORRECT);
    }
}
