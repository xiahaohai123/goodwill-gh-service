package com.wangkang.goodwillghservice.security.service;

import com.wangkang.goodwillghservice.dao.goodwillghservice.security.model.Permission;
import com.wangkang.goodwillghservice.dao.goodwillghservice.security.model.PermissionGroup;
import com.wangkang.goodwillghservice.dao.goodwillghservice.security.model.User;
import com.wangkang.goodwillghservice.dao.goodwillghservice.security.repository.UserRepository;
import com.wangkang.goodwillghservice.exception.BusinessException;
import com.wangkang.goodwillghservice.security.BuiltInPermissionGroup;
import com.wangkang.goodwillghservice.security.UserNotFoundException;
import com.wangkang.goodwillghservice.security.entity.CustomAuthenticationToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Set<GrantedAuthority> loadByCustomAuthenticationToken(CustomAuthenticationToken customAuthenticationToken) {
        if (customAuthenticationToken == null || StringUtils.isBlank(
                customAuthenticationToken.getAreaCode()) || StringUtils.isBlank(
                customAuthenticationToken.getPhoneNumber())) {
            throw new BusinessException("Invalid token");
        }
        return loadByPhone(customAuthenticationToken.getAreaCode(), customAuthenticationToken.getPhoneNumber());
    }

    public Set<GrantedAuthority> loadByPhone(String areaCode, String phoneNumber) throws UserNotFoundException {
        User user = userRepository.findByAreaCodeAndPhoneNumber(areaCode, phoneNumber);
        if (user == null) {
            throw new UserNotFoundException("User not exists");
        }
        boolean isAdmin = false;
        Set<PermissionGroup> groups = user.getGroups();
        for (PermissionGroup group : groups) {
            if (Boolean.TRUE.equals(group.getBuildIn()) && BuiltInPermissionGroup.ADMIN.name()
                    .equals(group.getName())) {
                isAdmin = true;
                break;
            }
        }
        Set<GrantedAuthority> authorities;
        if (isAdmin) {
            // 管理员拥有所有权限
            authorities = EnumSet.allOf(Permission.class)
                    .stream()
                    .map(Enum::name)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        } else {
            authorities = groups.stream()
                    .flatMap(g -> g.getPermissions().stream())
                    .map(Enum::name)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        }
        return authorities;
    }

}
