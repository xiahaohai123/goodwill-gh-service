package com.wangkang.goodwillghservice.feature.user.base.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StrongPasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StrongPassword {

    String message() default "{validation.password.strong}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}