package com.huysg136.cirquo_server.auth.exception;

import com.huysg136.cirquo_server.exception.AppException;
import com.huysg136.cirquo_server.exception.ErrorCode;

public class PasswordConfirmationMismatchException extends AppException {
    public PasswordConfirmationMismatchException() {
        super(ErrorCode.PASSWORD_CONFIRMATION_MISMATCH);
    }
}
