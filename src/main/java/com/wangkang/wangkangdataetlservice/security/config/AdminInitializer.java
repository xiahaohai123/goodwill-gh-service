package com.wangkang.wangkangdataetlservice.security.config;

import com.wangkang.wangkangdataetlservice.dao.dataetlservice.security.model.PermissionGroup;
import com.wangkang.wangkangdataetlservice.dao.dataetlservice.security.model.User;
import com.wangkang.wangkangdataetlservice.dao.dataetlservice.security.repository.PermissionGroupRepository;
import com.wangkang.wangkangdataetlservice.dao.dataetlservice.security.repository.UserRepository;
import com.wangkang.wangkangdataetlservice.security.BuiltInPermissionGroup;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

@Component
public class AdminInitializer {
    @Value("${app.init.password:root}")
    private String initPassword;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Log log = LogFactory.getLog(AdminInitializer.class);
    private final PermissionGroupRepository permissionGroupRepository;

    public AdminInitializer(UserRepository userRepository,
                            PasswordEncoder passwordEncoder, PermissionGroupRepository permissionGroupRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.permissionGroupRepository = permissionGroupRepository;
    }

    @EventListener
    protected void initAdmin(ApplicationReadyEvent event) {
        String constantAdmin = BuiltInPermissionGroup.ADMIN.name();
        String constantUserGroup = BuiltInPermissionGroup.USER.name();
        if (Boolean.FALSE.equals(permissionGroupRepository.existsPermissionGroupByName(constantUserGroup))) {
            PermissionGroup permissionGroup = new PermissionGroup();
            permissionGroup.setBuildIn(true);
            permissionGroup.setName(constantUserGroup);
            permissionGroup.setPermissions(Collections.emptySet());
            permissionGroupRepository.save(permissionGroup);
        }

        if (Boolean.FALSE.equals(permissionGroupRepository.existsPermissionGroupByName(constantAdmin))) {
            PermissionGroup permissionGroup = new PermissionGroup();
            permissionGroup.setBuildIn(true);
            permissionGroup.setName(constantAdmin);
            permissionGroup.setPermissions(Collections.emptySet());
            permissionGroupRepository.save(permissionGroup);
        }

        if (Boolean.FALSE.equals(userRepository.existsByUsernameIgnoreCase(constantAdmin))) {
            PermissionGroup adminGroup = permissionGroupRepository.findByName(constantAdmin);
            PermissionGroup userGroup = permissionGroupRepository.findByName(constantUserGroup);
            User admin = new User();
            admin.setUsername(constantAdmin);
            admin.setDisplayName(constantAdmin);
            admin.setPassword(passwordEncoder.encode(initPassword));
            admin.setBuildIn(true);
            admin.setGroups(Set.of(adminGroup, userGroup));
            userRepository.save(admin);
            log.info("Initialized account for admin: admin/" + initPassword);
        }
    }
}
