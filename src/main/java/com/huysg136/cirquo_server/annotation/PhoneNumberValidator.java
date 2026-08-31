package com.huysg136.cirquo_server.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    private static final java.util.regex.Pattern PHONE_PATTERN = Pattern.compile("^(?:0[35789]\\d{8}|\\+84[35789]\\d{8})$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()){
            return true;
        }

        return PHONE_PATTERN.matcher(value).matches();
    }
}
