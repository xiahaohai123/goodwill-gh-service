package com.wangkang.goodwillghservice.security.entity;

import org.springframework.security.core.GrantedAuthority;

import java.util.Set;
import java.util.UUID;

public class CustomUserDetails {
    private final UUID userId;
    private final String areaCode;
    private final String phoneNumber;
    private final Set<GrantedAuthority> grantedAuthorities;

    public CustomUserDetails(UUID userId,
                             String areaCode,
                             String phoneNumber,
                             Set<GrantedAuthority> grantedAuthorities) {
        this.userId = userId;
        this.areaCode = areaCode;
        this.phoneNumber = phoneNumber;
        this.grantedAuthorities = Set.copyOf(grantedAuthorities);
    }

    public UUID getUserId() {
        return userId;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Set<GrantedAuthority> getGrantedAuthorities() {
        return grantedAuthorities;
    }
}
