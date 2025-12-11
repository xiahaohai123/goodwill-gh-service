package com.wangkang.goodwillghservice.share.util;

import com.wangkang.goodwillghservice.core.exception.I18nBusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

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

    public static void notEmptyCollection(Collection<?> value, String msgCode, Object... args) {
        if (CollectionUtils.isEmpty(value)) {
            throw new I18nBusinessException(msgCode, args);
        }
    }
}
