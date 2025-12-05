package com.wangkang.goodwillghservice.security;

import org.springframework.security.core.AuthenticationException;

public class UserNotFoundException extends AuthenticationException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
