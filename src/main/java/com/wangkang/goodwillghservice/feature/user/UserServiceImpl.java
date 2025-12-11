package com.wangkang.goodwillghservice.feature.user;

import com.wangkang.goodwillghservice.core.exception.I18nBusinessException;
import com.wangkang.goodwillghservice.dao.goodwillghservice.security.model.PermissionGroup;
import com.wangkang.goodwillghservice.dao.goodwillghservice.security.model.User;
import com.wangkang.goodwillghservice.dao.goodwillghservice.security.repository.PermissionGroupRepository;
import com.wangkang.goodwillghservice.dao.goodwillghservice.security.repository.UserRepository;
import com.wangkang.goodwillghservice.security.BuiltInPermissionGroup;
import com.wangkang.goodwillghservice.share.util.BizAssert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
}
