package com.huysg136.cirquo_server.catalog.exception;

import com.huysg136.cirquo_server.exception.AppException;
import com.huysg136.cirquo_server.exception.ErrorCode;

public class ProductNotFoundException extends AppException {
    public ProductNotFoundException(){
        super(ErrorCode.PRODUCT_NOT_FOUND);
    }
}
