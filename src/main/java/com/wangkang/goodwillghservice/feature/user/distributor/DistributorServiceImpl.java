package com.wangkang.goodwillghservice.feature.user.distributor;

import com.wangkang.goodwillghservice.dao.goodwillghservice.security.model.User;
import com.wangkang.goodwillghservice.dao.goodwillghservice.security.repository.UserRepository;
import com.wangkang.goodwillghservice.feature.user.UserDTO;
import com.wangkang.goodwillghservice.security.BuiltInPermissionGroup;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistributorServiceImpl implements DistributorService {

    private final UserRepository userRepository;

    public DistributorServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDTO> getDistributors() {
        List<User> distinctByGroupsName = userRepository.findDistinctByGroups_Name(BuiltInPermissionGroup.DISTRIBUTOR.name());
        return distinctByGroupsName.stream().map(user -> {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            return userDTO;
        }).toList();
    }

    @Override
    public void updateDistributorExternal() {

    }
}
