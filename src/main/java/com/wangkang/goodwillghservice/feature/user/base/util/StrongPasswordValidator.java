package com.wangkang.goodwillghservice.feature.user.base.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StrongPasswordValidator
        implements ConstraintValidator<StrongPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) return false;

        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[@$!%*?&].*");
        boolean lengthOk = password.length() >= 8 && password.length() <= 20;

        return hasUpper && hasLower && hasDigit && hasSpecial && lengthOk;
    }
}