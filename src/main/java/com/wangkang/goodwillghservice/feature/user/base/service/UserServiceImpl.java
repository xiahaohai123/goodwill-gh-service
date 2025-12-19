package com.wangkang.goodwillghservice.feature.user.base.service;

import com.wangkang.goodwillghservice.core.exception.I18nBusinessException;
import com.wangkang.goodwillghservice.dao.goodwillghservice.security.model.PermissionGroup;
import com.wangkang.goodwillghservice.dao.goodwillghservice.security.model.User;
import com.wangkang.goodwillghservice.dao.goodwillghservice.security.repository.PermissionGroupRepository;
import com.wangkang.goodwillghservice.dao.goodwillghservice.security.repository.UserRepository;
import com.wangkang.goodwillghservice.feature.audit.entity.ActionType;
import com.wangkang.goodwillghservice.feature.audit.entity.Auditable;
import com.wangkang.goodwillghservice.feature.user.base.entity.PasswordUpdateDTO;
import com.wangkang.goodwillghservice.feature.user.base.entity.UserDTO;
import com.wangkang.goodwillghservice.feature.user.base.util.NicknameGenerator;
import com.wangkang.goodwillghservice.security.BuiltInPermissionGroup;
import com.wangkang.goodwillghservice.share.util.BizAssert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PermissionGroupRepository permissionGroupRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, PermissionGroupRepository permissionGroupRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.permissionGroupRepository = permissionGroupRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Auditable(actionType = ActionType.USER,actionName = "Register user")
    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        BizAssert.notNull(userDTO, "user.null");
        String areaCode = userDTO.getAreaCode();
        String phoneNumber = userDTO.getPhoneNumber();
        Set<BuiltInPermissionGroup> roles = userDTO.getRoles();
        BizAssert.notBlank(areaCode, "user.areaCode.required");
        BizAssert.notBlank(phoneNumber, "user.phone.required");
        BizAssert.notBlank(userDTO.getPassword(), "user.password.required");
        BizAssert.notEmptyCollection(roles, "user.role.required");
        // 已存在，不可注册
        if (Boolean.TRUE.equals(userRepository.existsByAreaCodeAndPhoneNumber(areaCode, phoneNumber))) {
            throw new I18nBusinessException("user.phone.exists", areaCode, phoneNumber);
        }
        if (StringUtils.isBlank(userDTO.getDisplayName())) {
            userDTO.setDisplayName(NicknameGenerator.generateNickname());
        }
        roles.add(BuiltInPermissionGroup.USER);
        List<String> roleNameList = roles.stream().map(BuiltInPermissionGroup::name).distinct().toList();
        List<PermissionGroup> roleGroup = permissionGroupRepository.findByNameIn(roleNameList);
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setGroups(new HashSet<>(roleGroup));
        User savedUser = userRepository.save(user);
        UserDTO resultUser = new UserDTO();
        BeanUtils.copyProperties(savedUser, resultUser);
        resultUser.setRoles(roles);
        resultUser.erasePassword();
        return resultUser;
    }

    @Auditable(actionType = ActionType.USER,actionName = "Update password")
    @Override
    public void updatePassword(PasswordUpdateDTO passwordUpdateDTO, Principal principal) {
        // 1. 解析当前用户
        UUID userId;
        try {
            userId = UUID.fromString(principal.getName());
        } catch (IllegalArgumentException e) {
            throw new I18nBusinessException("auth.principal.invalid");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new I18nBusinessException("user.not.found"));

        // 2. 校验旧密码是否正确
        if (!passwordEncoder.matches(
                passwordUpdateDTO.getOldPassword(),
                user.getPassword())) {
            throw new I18nBusinessException("user.password.old.incorrect");
        }

        // 3. 新旧密码不能相同
        if (passwordEncoder.matches(
                passwordUpdateDTO.getNewPassword(),
                user.getPassword())) {
            throw new I18nBusinessException("user.password.same.as.old");
        }

        // 4. 更新密码
        user.setPassword(
                passwordEncoder.encode(passwordUpdateDTO.getNewPassword())
        );

        userRepository.save(user);
    }
}
