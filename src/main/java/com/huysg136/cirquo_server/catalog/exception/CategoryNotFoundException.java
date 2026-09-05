package com.huysg136.cirquo_server.catalog.exception;

import com.huysg136.cirquo_server.exception.AppException;
import com.huysg136.cirquo_server.exception.ErrorCode;

public class CategoryNotFoundException extends AppException {
    public CategoryNotFoundException(){
        super(ErrorCode.CATEGORY_NOT_FOUND);
    }
}
