package com.wangkang.goodwillghservice.share.locale;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Locale;

@Component
public class LocaleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        // 根据 Accept-Language 自动解析
        Locale locale = request.getLocale();
        if (locale == null) {
            locale = Locale.ENGLISH;
        }

        LocaleContext.setLocale(locale);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        // 避免 ThreadLocal 泄漏
        LocaleContext.clear();
    }
}
