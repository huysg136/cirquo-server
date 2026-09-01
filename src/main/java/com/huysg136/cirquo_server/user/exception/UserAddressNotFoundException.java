package com.huysg136.cirquo_server.user.exception;

import com.huysg136.cirquo_server.exception.AppException;
import com.huysg136.cirquo_server.exception.ErrorCode;

public class UserAddressNotFoundException extends AppException {

    public UserAddressNotFoundException() {
        super(ErrorCode.ADDRESS_NOT_FOUND);
    }

}
