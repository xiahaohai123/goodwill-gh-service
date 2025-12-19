package com.wangkang.goodwillghservice.feature.user.base.entity;

import com.wangkang.goodwillghservice.feature.user.base.util.StrongPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class RegisterDTO {
    /** 手机号区号 */
    @NotBlank
    @Pattern(regexp = "\\+233|\\+86", message = "{user.areaCode.invalid}")
    private String areaCode;
    /** 手机号 */
    @NotBlank
    @Pattern(regexp = "\\d{6,15}", message = "{user.phone.invalid}")
    private String phoneNumber;
    /** 密码 */
    @NotBlank
    @StrongPassword
    private String password;
    /** 邀请码 */
    @NotBlank
    private String invitationCode;
    /** 显示昵称 */
    private String displayName;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
