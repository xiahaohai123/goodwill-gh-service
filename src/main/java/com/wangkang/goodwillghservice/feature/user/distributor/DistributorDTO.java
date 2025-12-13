package com.wangkang.goodwillghservice.feature.user.distributor;

import com.wangkang.goodwillghservice.security.BuiltInPermissionGroup;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DistributorDTO {
    private UUID id;
    private String areaCode;
    private String phoneNumber;
    private String displayName;

    private Set<BuiltInPermissionGroup> roles = new HashSet<>();
    private UUID inviterId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public Set<BuiltInPermissionGroup> getRoles() {
        return roles;
    }

    public void setRoles(Set<BuiltInPermissionGroup> roles) {
        this.roles = roles;
    }

    public UUID getInviterId() {
        return inviterId;
    }

    public void setInviterId(UUID inviterId) {
        this.inviterId = inviterId;
    }
}
