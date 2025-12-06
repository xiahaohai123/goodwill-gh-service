package com.wangkang.goodwillghservice.feature.user;

import com.wangkang.goodwillghservice.security.BuiltInPermissionGroup;

import java.time.OffsetDateTime;
import java.util.UUID;

/** 用户邀请函 */
public class Invitation {
    private String code;
    private BuiltInPermissionGroup role;
    private OffsetDateTime expireAt;
    private UUID inviterId;

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

    public UUID getInviterId() {
        return inviterId;
    }

    public void setInviterId(UUID inviterId) {
        this.inviterId = inviterId;
    }
}
