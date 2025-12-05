package com.wangkang.goodwillghservice.locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

/** 多语言文本 code 获取真实文本的服务 */
@Service
public class MessageService {

    private final MessageSource messageSource;

    @Autowired
    public MessageService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String code) {
        Locale locale = LocaleContext.getLocale();
        return messageSource.getMessage(code, null, locale);
    }

    public String getMessage(String code, Object... args) {
        Locale locale = LocaleContext.getLocale();
        return messageSource.getMessage(code, args, locale);
    }
}
