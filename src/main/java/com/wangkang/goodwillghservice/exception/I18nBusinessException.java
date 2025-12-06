package com.wangkang.goodwillghservice.exception;

public class I18nBusinessException extends RuntimeException {
    private final String code;      // i18n key，如 "user.phone.required"
    private final transient Object[] args;    // 动态参数，用于 message 格式化

    public I18nBusinessException(String code) {
        super(code);
        this.code = code;
        this.args = null;
    }

    public I18nBusinessException(String code, Object... args) {
        super(code);
        this.code = code;
        this.args = args;
    }

    public String getCode() {
        return code;
    }

    public Object[] getArgs() {
        return args;
    }
}
