package com.wangkang.goodwillghservice.dao.goodwillghservice.security.repository;

import com.wangkang.goodwillghservice.dao.goodwillghservice.security.model.PermissionGroup;
import com.wangkang.goodwillghservice.dao.goodwillghservice.security.model.User;
import com.wangkang.goodwillghservice.feature.user.distributor.Distributor4ManagerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
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
    Page<User> findDistinctByGroups_Name(String groupName, Pageable pageable);

    User findByIdAndGroups_Name(UUID id, String groupName);

    @Query("""
            SELECT new com.wangkang.goodwillghservice.feature.user.distributor.Distributor4ManagerDTO(
                u.id,
                u.areaCode,
                u.phoneNumber,
                u.displayName,
                e.externalName
            )
            FROM User u
            JOIN u.groups g
            LEFT JOIN u.distributorProfile p
            LEFT JOIN p.externalDistributor e
            WHERE g.name = "DISTRIBUTOR"
            """)
    Page<Distributor4ManagerDTO> findDistributorWithExternalInfo(Pageable pageable);
}
