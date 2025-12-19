package com.wangkang.goodwillghservice.feature.user.base.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wangkang.goodwillghservice.feature.audit.entity.Sensitive;
import com.wangkang.goodwillghservice.security.BuiltInPermissionGroup;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private String areaCode;
    private String phoneNumber;
    private String displayName;
    @Sensitive
    private String password;


    private Set<BuiltInPermissionGroup> roles = new HashSet<>();
    private UUID inviterId;

    public Set<BuiltInPermissionGroup> getRoles() {
        return roles;
    }

    public void setRoles(Set<BuiltInPermissionGroup> roles) {
        this.roles = roles;
    }

    public void addRole(BuiltInPermissionGroup role) {
        this.roles.add(role);
    }

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
