package com.wangkang.goodwillghservice.security.service;

import com.wangkang.goodwillghservice.dao.goodwillghservice.security.model.Permission;
import com.wangkang.goodwillghservice.dao.goodwillghservice.security.model.PermissionGroup;
import com.wangkang.goodwillghservice.dao.goodwillghservice.security.model.User;
import com.wangkang.goodwillghservice.dao.goodwillghservice.security.repository.UserRepository;
import com.wangkang.goodwillghservice.security.BuiltInPermissionGroup;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameIgnoreCase(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
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

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
