package com.example.userservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {
    private static final String PHONE_PATTERN = "^\\+?[0-9]{7,15}$";

    @Override
    public boolean isValid(String phoneField, ConstraintValidatorContext context) {
        if (phoneField == null || phoneField.isEmpty()) {
            return true;
        }
        return phoneField.matches(PHONE_PATTERN);
    }
}