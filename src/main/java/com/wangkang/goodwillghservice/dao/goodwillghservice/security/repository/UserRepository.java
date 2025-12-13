package com.wangkang.goodwillghservice.dao.goodwillghservice.security.repository;

import com.wangkang.goodwillghservice.dao.goodwillghservice.security.model.PermissionGroup;
import com.wangkang.goodwillghservice.dao.goodwillghservice.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>,
        JpaSpecificationExecutor<User> {

    Boolean existsByAreaCodeAndPhoneNumber(String areaCode, String phoneNumber);

    User findByAreaCodeAndPhoneNumber(String areaCode, String phoneNumber);

    List<User> findUserByGroupsIn(Collection<Set<PermissionGroup>> groups);

    // 返回不重复的用户
    List<User> findDistinctByGroups_Name(String groupName);

    User findByIdAndGroups_Name(UUID id, String groupName);
}
