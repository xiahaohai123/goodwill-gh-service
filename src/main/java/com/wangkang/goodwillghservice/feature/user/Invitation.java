package com.wangkang.goodwillghservice.feature.user;

import java.time.OffsetDateTime;

/** 用户邀请函 */
public class Invitation {
    private String code;
    private String role;
    private OffsetDateTime expireAt;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public OffsetDateTime getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(OffsetDateTime expireAt) {
        this.expireAt = expireAt;
    }
}
