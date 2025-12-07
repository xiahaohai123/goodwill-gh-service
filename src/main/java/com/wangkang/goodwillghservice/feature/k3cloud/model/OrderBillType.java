package com.wangkang.goodwillghservice.feature.k3cloud.model;

import java.util.Map;

public enum OrderBillType {
    STANDARD("XSDD01_SYS", "标准销售订单"),
    DISTRIBUTION("XSDD07_SYS", "分销购销订单");

    public final String code;
    public final String description;

    OrderBillType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static final Map<String, String> ORDER_TYPE_CODE_DESC = Map.of(
            STANDARD.code, STANDARD.description,
            DISTRIBUTION.code, DISTRIBUTION.description
    );
}
