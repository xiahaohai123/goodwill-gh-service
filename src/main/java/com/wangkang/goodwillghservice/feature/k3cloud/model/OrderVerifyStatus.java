package com.wangkang.goodwillghservice.feature.k3cloud.model;

import java.util.Map;

public enum OrderVerifyStatus {
    VERIFIED("0", "未复核"),
    UNVERIFIED("1", "已复核"),
    ;

    private static final Map<String, OrderVerifyStatus> CODE_2_STATUS_MAP = Map.of(
            VERIFIED.code, VERIFIED,
            UNVERIFIED.code, UNVERIFIED);

    public static final Map<String, String> VERIFY_CODE_DESC = Map.of(
            VERIFIED.code, VERIFIED.description,
            UNVERIFIED.code, UNVERIFIED.description);

    public final String code;
    public final String description;

    OrderVerifyStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static OrderVerifyStatus fromCode(String code) {
        if (code == null) {
            return null;
        }
        return CODE_2_STATUS_MAP.get(code);
    }
}
