package com.wangkang.goodwillghservice.locale;

import java.util.Locale;

/** 语言上下文，线程级别，拦截器内初始化，结束后清理 */
public class LocaleContext {
    private static final ThreadLocal<Locale> localeHolder = new ThreadLocal<>();

    public static void setLocale(Locale locale) {
        localeHolder.set(locale);
    }

    public static Locale getLocale() {
        return localeHolder.get() != null ? localeHolder.get() : Locale.ENGLISH;
    }

    public static void clear() {
        localeHolder.remove();
    }

    private LocaleContext() {}
}
