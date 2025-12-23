package com.wangkang.goodwillghservice.feature.user.tiler;

import com.wangkang.goodwillghservice.dao.goodwillghservice.security.model.User;
import com.wangkang.goodwillghservice.dao.goodwillghservice.security.repository.UserRepository;
import com.wangkang.goodwillghservice.security.BuiltInPermissionGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TilerServiceImpl implements TilerService {

    private final UserRepository userRepository;

    public TilerServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<User> getTilerPage(Pageable pageable) {
        Page<User> userPage = userRepository.findDistinctByGroups_Name(BuiltInPermissionGroup.TILER.name(),
                pageable);
        return userPage.map(user -> {
            user.setPassword(null);
            return user;
        });
    }
}
