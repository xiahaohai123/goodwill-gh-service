package com.wangkang.goodwillghservice.feature.sms;

import com.wangkang.goodwillghservice.feature.audit.entity.ActionType;
import com.wangkang.goodwillghservice.feature.audit.entity.Auditable;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;


@Service
public class VerificationCodeService {

    private final SmsProviderManager smsProviderManager;
    private final SmsTemplate smsTemplate;

    public VerificationCodeService(SmsProviderManager smsProviderManager, SmsTemplate smsTemplate) {
        this.smsProviderManager = smsProviderManager;
        this.smsTemplate = smsTemplate;
    }

    @Auditable(actionType = ActionType.SMS, actionName = "Send message to a phone")
    public String sendVerificationCode(String areaCode, String phoneNumber) {
        String code = generateCode();
        String message = smsTemplate.verificationCode(code);
        smsProviderManager.sendSms(areaCode, phoneNumber, message);
        return code;
    }

    private String generateCode() {
        int code = RandomUtils.secure().randomInt(100000, 1000000);
        return String.valueOf(code);
    }
}