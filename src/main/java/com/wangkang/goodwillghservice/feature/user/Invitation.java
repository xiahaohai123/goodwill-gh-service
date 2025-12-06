package com.wangkang.goodwillghservice.feature.user;

import com.wangkang.goodwillghservice.security.BuiltInPermissionGroup;

import java.time.OffsetDateTime;

/** 用户邀请函 */
public class Invitation {
    private String code;
    private BuiltInPermissionGroup role;
    private OffsetDateTime expireAt;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BuiltInPermissionGroup getRole() {
        return role;
    }

    public void setRole(BuiltInPermissionGroup role) {
        this.role = role;
    }

    public OffsetDateTime getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(OffsetDateTime expireAt) {
        this.expireAt = expireAt;
    }
}
