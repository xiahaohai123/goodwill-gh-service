package com.wangkang.goodwillghservice.security.config;

import com.wangkang.goodwillghservice.dao.goodwillghservice.security.model.PermissionGroup;
import com.wangkang.goodwillghservice.dao.goodwillghservice.security.model.User;
import com.wangkang.goodwillghservice.dao.goodwillghservice.security.repository.PermissionGroupRepository;
import com.wangkang.goodwillghservice.dao.goodwillghservice.security.repository.UserRepository;
import com.wangkang.goodwillghservice.security.BuiltInPermissionGroup;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

/** 账号和权限等初始化器 */
@Component
public class AuthInitializer {
    // 内置超级管理员手机号和区号（可配置或从环境变量读取）
    @Value("${app.init.admin.phoneNumber:10000000000}")
    private String adminPhone;
    @Value("${app.init.admin.areaCode:+86}")
    private String adminAreaCode;
    @Value("${app.init.admin.password:root}")
    private String initPassword;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Log log = LogFactory.getLog(AuthInitializer.class);
    private final PermissionGroupRepository permissionGroupRepository;

    public AuthInitializer(UserRepository userRepository,
                           PasswordEncoder passwordEncoder, PermissionGroupRepository permissionGroupRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.permissionGroupRepository = permissionGroupRepository;
    }

    @EventListener
    protected void initAdmin(ApplicationReadyEvent event) {
        String constantAdmin = BuiltInPermissionGroup.ADMIN.name();
        String constantUserGroup = BuiltInPermissionGroup.USER.name();

        if (Boolean.FALSE.equals(userRepository.existsByAreaCodeAndPhoneNumber(adminAreaCode, adminPhone))) {
            PermissionGroup adminGroup = permissionGroupRepository.findByName(constantAdmin);
            PermissionGroup userGroup = permissionGroupRepository.findByName(constantUserGroup);
            User admin = new User();
            admin.setPhoneNumber(adminPhone);
            admin.setAreaCode(adminAreaCode);
            admin.setDisplayName(constantAdmin);
            admin.setPassword(passwordEncoder.encode(initPassword));
            admin.setBuildIn(true);
            admin.setGroups(Set.of(adminGroup, userGroup));
            admin.setDeleted(false);
            userRepository.save(admin);
            log.info("Initialized account for admin: admin/" + initPassword);
        }
    }
}
