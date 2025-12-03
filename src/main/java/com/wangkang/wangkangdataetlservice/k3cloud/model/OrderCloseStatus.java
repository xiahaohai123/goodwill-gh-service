package com.wangkang.wangkangdataetlservice.k3cloud.model;

import java.util.Map;

public enum OrderCloseStatus {

    NORMAL("A", "正常"),
    CLOSED("B", "已关闭");
    private static final Map<String, OrderCloseStatus> CODE_2_STATUS_MAP = Map.of(
            NORMAL.code, NORMAL,
            CLOSED.code, CLOSED);

    public final String code;
    public final String description;

    OrderCloseStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static final Map<String ,String> CLOSE_STATUS_CODE_2_NAME = Map.of(
            NORMAL.code, NORMAL.description,
            CLOSED.code, CLOSED.description
    );

    public static OrderCloseStatus fromCode(String code) {
        if (code == null) {
            return null;
        }
        return CODE_2_STATUS_MAP.get(code);
    }
}
