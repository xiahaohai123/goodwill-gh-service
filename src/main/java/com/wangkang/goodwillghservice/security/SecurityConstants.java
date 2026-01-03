package com.wangkang.goodwillghservice.security;

import java.util.List;

public class SecurityConstants {
    private SecurityConstants() {
    }

    public static final List<String> WHITE_LIST = List.of(
            "/api/download",
            "/api/auth/login",
            "/api/user/register",
            "/api/user/invitation/profile/**",
            "/api/geo/ghana/hierarchy"
    );
}
