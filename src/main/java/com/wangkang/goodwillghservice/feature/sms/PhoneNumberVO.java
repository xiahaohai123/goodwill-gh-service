package com.wangkang.goodwillghservice.feature.sms;

import jakarta.validation.constraints.NotBlank;

public class PhoneNumberVO {
    @NotBlank
    private String areaCode;
    @NotBlank
    private String phoneNumber;

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
