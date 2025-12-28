package com.wangkang.goodwillghservice.feature.k3cloud.model;

import java.util.Map;

public enum OrderDocumentStatus {

    CREATED("A", "创建", "order.documentStatus.created"),
    UNDER_REVIEW("B", "审核中", "order.documentStatus.underReview"),
    REVIEWED("C", "已审核", "order.documentStatus.reviewed"),
    RE_REVIEW("D", "重新审核", "order.documentStatus.reReview"),
    TMP_STORE("Z", "暂存", "order.documentStatus.tmpStore"),
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
    /** 用于多语言资源串的索引 */
    public final String descriptionCode;

    OrderDocumentStatus(String code, String description, String descriptionCode) {
        this.code = code;
        this.description = description;
        this.descriptionCode = descriptionCode;
    }

    public static String code2Description(String code) {
        if (code == null) {
            return null;
        }
        return DOCUMENT_STATUS_CODE_2_DESC.get(code);
    }

    public static OrderDocumentStatus fromCode(String code) {
        return code2InstanceMapping.get(code);
    }
}
