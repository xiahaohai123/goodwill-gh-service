package com.wangkang.wangkangdataetlservice.audit.entity;

public enum ActionType {
    EXPORT("Export"),
    CONFIG("Config"),
    TRUCK_DISPATCH("Truck Dispatch"),
    VEHICLE_LOG("Vehicle Log"),
    SALE_ORDER("Sale Order"),
    SALE("Sale"),
    BI("Business Intelligence"),
    OTHER_DATA("Other Data"),
    PRODUCT("Product"),
    SEA_FREIGHT("Sea freight"),
    ;

    ActionType(String value) {
        this.value = value;
    }

    public final String value;
}
