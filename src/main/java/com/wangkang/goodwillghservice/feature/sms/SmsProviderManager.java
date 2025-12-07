package com.wangkang.goodwillghservice.feature.sms;

import com.wangkang.goodwillghservice.core.exception.I18nBusinessException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SmsProviderManager {

    private final List<SmsProvider> providers;

    public SmsProviderManager(List<SmsProvider> providers) {
        this.providers = providers;
    }

    public void sendSms(String areaCode, String phoneNumber, String message) {
        for (SmsProvider provider : providers) {
            if (provider.supports(areaCode)) {
                provider.sendSms(areaCode, phoneNumber, message);
                return;
            }
        }
        throw new I18nBusinessException("sms.provider.notfound", phoneNumber);
    }
}