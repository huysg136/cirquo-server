package com.huysg136.cirquo_server.catalog.exception;

import com.huysg136.cirquo_server.exception.AppException;
import com.huysg136.cirquo_server.exception.ErrorCode;

public class ProductSlugAlreadyExistsException extends AppException {
    public ProductSlugAlreadyExistsException(){
        super(ErrorCode.CATEGORY_SLUG_ALREADY_EXISTS);
    }
}
