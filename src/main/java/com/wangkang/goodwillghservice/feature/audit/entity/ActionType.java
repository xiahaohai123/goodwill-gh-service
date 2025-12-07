package com.wangkang.goodwillghservice.feature.audit.entity;

public enum ActionType {
    CONFIG("Config"),
    USER("User"),
    SMS("SMS"),
    ;

    ActionType(String value) {
        this.value = value;
    }

    public final String value;
}
