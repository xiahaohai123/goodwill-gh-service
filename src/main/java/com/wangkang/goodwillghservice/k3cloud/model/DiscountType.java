package com.wangkang.goodwillghservice.k3cloud.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum DiscountType {
    CASH_DISCOUNT("现金折扣", "D"),
    RETURN_GOODS("返货", "A"),
    GIVEAWAY("赠品", "C"),
    GIFT("礼品", "B"),
    ADVERTISING("广告宣传", "E"),
    FREIGHT_SUBSIDY("运费补贴", "G"),
    SALES_SAMPLE("销售样品", "H"),
    PRICE_SUBSIDY("价格补贴", "F"),
    COMPENSATION_FOR_DAMAGES("货损赔偿", "I"),
    HANDLE_TAILS_GOODS("处理尾货", "J"),
    PAYMENT_DISCOUNT("付款折扣", "K"),
    ;

    public static final Map<String, String> DISCOUNT_DESCRIPTION_2_CODE;
    public static final Map<String, String> DISCOUNT_CODE_2_DESCRIPTION;

    static {
        DISCOUNT_DESCRIPTION_2_CODE = Map.ofEntries(
                Map.entry(CASH_DISCOUNT.description, CASH_DISCOUNT.code),
                Map.entry(RETURN_GOODS.description, RETURN_GOODS.code),
                Map.entry(GIVEAWAY.description, GIVEAWAY.code),
                Map.entry(GIFT.description, GIFT.code),
                Map.entry(ADVERTISING.description, ADVERTISING.code),
                Map.entry(FREIGHT_SUBSIDY.description, FREIGHT_SUBSIDY.code),
                Map.entry(SALES_SAMPLE.description, SALES_SAMPLE.code),
                Map.entry(PRICE_SUBSIDY.description, PRICE_SUBSIDY.code),
                Map.entry(COMPENSATION_FOR_DAMAGES.description, COMPENSATION_FOR_DAMAGES.code),
                Map.entry(HANDLE_TAILS_GOODS.description, HANDLE_TAILS_GOODS.code),
                Map.entry(PAYMENT_DISCOUNT.description, PAYMENT_DISCOUNT.code)
        );

        Map<String, String> tempDiscountCode2Name = new HashMap<>();
        for (Map.Entry<String, String> entry : DISCOUNT_DESCRIPTION_2_CODE.entrySet()) {
            tempDiscountCode2Name.put(entry.getValue(), entry.getKey());
        }
        DISCOUNT_CODE_2_DESCRIPTION = Collections.unmodifiableMap(tempDiscountCode2Name);
    }


    DiscountType(String description, String code) {
        this.description = description;
        this.code = code;
    }

    public final String description;
    public final String code;

    public static String getDescription(String code) {
        if (code == null) {
            return null;
        }
        return DISCOUNT_CODE_2_DESCRIPTION.get(code);
    }

    public static String getCode(String description) {
        if (description == null) {
            return null;
        }
        return DISCOUNT_DESCRIPTION_2_CODE.get(description);
    }
}
