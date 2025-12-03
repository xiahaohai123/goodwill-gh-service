package com.wangkang.wangkangdataetlservice.dao.dataetlservice.security.repository;

import com.wangkang.wangkangdataetlservice.dao.dataetlservice.security.model.PermissionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PermissionGroupRepository extends JpaRepository<PermissionGroup, UUID> {

    Boolean existsPermissionGroupByName(String name);

    PermissionGroup findByName(String name);
}
