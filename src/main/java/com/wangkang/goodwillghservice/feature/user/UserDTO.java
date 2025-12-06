package com.wangkang.goodwillghservice.feature.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wangkang.goodwillghservice.security.BuiltInPermissionGroup;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private String areaCode;
    private String phoneNumber;
    private String displayName;
    private String password;
    private BuiltInPermissionGroup role;
    private UUID inviterId;

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BuiltInPermissionGroup getRole() {
        return role;
    }

    public void setRole(BuiltInPermissionGroup role) {
        this.role = role;
    }

    public void erasePassword() {
        this.password = null;
    }

    public UUID getInviterId() {
        return inviterId;
    }

    public void setInviterId(UUID inviterId) {
        this.inviterId = inviterId;
    }
}
