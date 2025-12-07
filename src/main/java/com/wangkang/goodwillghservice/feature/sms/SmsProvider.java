package com.wangkang.goodwillghservice.feature.sms;

public interface SmsProvider {

    /**
     * 发送消息给某个电话，注意，是 IO 操作，最好异步操作
     * @param areaCode    区号
     * @param phoneNumber 电话号码
     * @param message     消息
     */
    void sendSms(String areaCode, String phoneNumber, String message);

    /**
     * 判断此策略是否适用该区号
     */
    boolean supports(String areaCode);
}
