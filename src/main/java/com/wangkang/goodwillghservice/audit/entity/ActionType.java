package com.wangkang.goodwillghservice.audit.entity;

public enum ActionType {
    CONFIG("Config"),
    USER("User"),
    ;

    ActionType(String value) {
        this.value = value;
    }

    public final String value;
}
