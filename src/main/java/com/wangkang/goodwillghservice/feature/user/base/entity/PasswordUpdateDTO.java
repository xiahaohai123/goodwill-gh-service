package com.wangkang.goodwillghservice.feature.user.base.entity;

import com.wangkang.goodwillghservice.feature.audit.entity.Sensitive;
import com.wangkang.goodwillghservice.feature.user.base.util.StrongPassword;
import jakarta.validation.constraints.NotBlank;

public class PasswordUpdateDTO {

    @NotBlank
    @Sensitive
    private String oldPassword;
    @NotBlank
    @StrongPassword
    @Sensitive
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
