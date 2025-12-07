package com.wangkang.goodwillghservice.feature.sms;

import org.springframework.stereotype.Component;

@Component
public class SmsTemplate {

    private static final String DOMAIN_INFO = "[GoodWill]";

    public String verificationCode(String code) {
        return DOMAIN_INFO + " Your verification code is: " + code;
    }

    // 可以增加其他类型模板
}