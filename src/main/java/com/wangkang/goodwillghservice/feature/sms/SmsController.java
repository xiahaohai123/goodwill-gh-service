package com.wangkang.goodwillghservice.feature.sms;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sms")
public class SmsController {

    private final VerificationCodeService verificationCodeService;

    public SmsController(VerificationCodeService verificationCodeService) {
        this.verificationCodeService = verificationCodeService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')") // 仅允许 ADMIN 角色访问
    @PostMapping("/send")
    public String sendVerificationCode(@Valid @RequestBody PhoneNumberVO phoneNumberVO) {
        return verificationCodeService.sendVerificationCode(phoneNumberVO.getAreaCode().strip(),
                phoneNumberVO.getPhoneNumber().strip());
    }
}