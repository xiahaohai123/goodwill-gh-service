package com.wangkang.goodwillghservice.feature.user.distributor;

import org.springframework.hateoas.server.core.Relation;

import java.util.UUID;

@Relation(collectionRelation = "items", itemRelation = "item")
public class Distributor4ManagerDTO {
    private UUID id;
    private String areaCode;
    private String phoneNumber;
    private String displayName;

    /** 绑定的 ERP 经销商名称，未绑定则为 null */
    private String externalName;

    public Distributor4ManagerDTO() {
    }

    public Distributor4ManagerDTO(UUID id, String areaCode, String phoneNumber, String displayName, String externalName) {
        this.id = id;
        this.areaCode = areaCode;
        this.phoneNumber = phoneNumber;
        this.displayName = displayName;
        this.externalName = externalName;
    }

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

    public String getExternalName() {
        return externalName;
    }

    public void setExternalName(String externalName) {
        this.externalName = externalName;
    }
}
