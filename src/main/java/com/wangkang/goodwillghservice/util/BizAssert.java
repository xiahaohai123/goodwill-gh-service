package com.wangkang.goodwillghservice.util;

import com.wangkang.goodwillghservice.exception.I18nBusinessException;
import org.apache.commons.lang3.StringUtils;

public class BizAssert {
    private BizAssert() {
    }

    public static void notNull(Object value, String msgCode, Object... args) {
        if (value == null) {
            throw new I18nBusinessException(msgCode, args);
        }
    }

    public static void notBlank(String value, String msgCode, Object... args) {
        if (StringUtils.isBlank(value)) {
            throw new I18nBusinessException(msgCode, args);
        }
    }
}
