package com.wangkang.goodwillghservice.feature.audit.entity;

public enum ActionType {
    CONFIG("Config"),
    USER("User"),
    SMS("SMS"),
    DISTRIBUTOR("Distributor"),
    PRODUCT("Product"),
    K3_ORDER("K3_Order"),
    TILER_SALES("Tiler Sales"),
    ;

    ActionType(String value) {
        this.value = value;
    }

    public final String value;
}
