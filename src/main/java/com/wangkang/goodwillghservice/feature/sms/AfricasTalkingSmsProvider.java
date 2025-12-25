package com.wangkang.goodwillghservice.feature.sms;

import com.wangkang.goodwillghservice.core.exception.I18nBusinessException;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AfricasTalkingSmsProvider implements SmsProvider {

    @Value("${sms.africastalking.apiKey}")
    private String apiKey;

    @Value("${sms.africastalking.username}")
    private String username;

    @Value("${sms.africastalking.host}")
    private String host;

    private final OkHttpClient client;

    public AfricasTalkingSmsProvider(@Qualifier("secureOkHttpClient") OkHttpClient client) {
        this.client = client;
    }

    @Override
    public boolean supports(String areaCode) {
        if (StringUtils.isBlank(areaCode)) {
            return false;
        }
        // 去掉开头的 '+' 符号
        String code = areaCode.startsWith("+") ? areaCode.substring(1) : areaCode;

        // 233(GH), 234(NG), 254(KE)
        return "233".equals(code) || "234".equals(code) || "254".equals(code);
    }

    @Override
    public void sendSms(String areaCode, String phoneNumber, String message) {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(host)
                .addPathSegments("version1/messaging")
                .build();

        String phoneTo = areaCode + phoneNumber;
        FormBody formBody = new FormBody.Builder()
                .add("username", username)
                .add("to", phoneTo)
                .add("message", message)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .addHeader("apiKey", apiKey)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";

            if (!response.isSuccessful()) {
                // 抛出自定义异常，支持多语言 key
                throw new I18nBusinessException("sms.send.failed", phoneNumber, responseBody);
            }
        } catch (IOException e) {
            throw new I18nBusinessException("sms.send.error", phoneNumber, e.getMessage());
        }
    }
}