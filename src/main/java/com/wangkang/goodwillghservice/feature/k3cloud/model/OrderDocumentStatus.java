package com.wangkang.goodwillghservice.feature.k3cloud.model;

import java.util.Map;

public enum OrderDocumentStatus {

    CREATED("A", "创建"),
    UNDER_REVIEW("B", "审核中"),
    REVIEWED("C", "已审核"),
    RE_REVIEW("D", "重新审核"),
    ;

    public static final Map<String, String> DOCUMENT_STATUS_CODE_2_DESC = Map.of(
            CREATED.code, CREATED.description,
            UNDER_REVIEW.code, UNDER_REVIEW.description,
            REVIEWED.code, REVIEWED.description,
            RE_REVIEW.code, RE_REVIEW.description
    );

    public static final Map<String, OrderDocumentStatus> code2InstanceMapping = Map.of(
            CREATED.code, CREATED,
            UNDER_REVIEW.code, UNDER_REVIEW,
            REVIEWED.code, REVIEWED,
            RE_REVIEW.code, RE_REVIEW
    );
    public final String code;
    public final String description;

    OrderDocumentStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static String code2Description(String code) {
        if (code == null) {
            return null;
        }
        return DOCUMENT_STATUS_CODE_2_DESC.get(code);
    }

    public static OrderDocumentStatus fromCode(String code) {
        return  code2InstanceMapping.get(code);
    }
}
