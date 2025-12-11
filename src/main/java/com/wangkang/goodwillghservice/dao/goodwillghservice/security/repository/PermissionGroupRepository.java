package com.wangkang.goodwillghservice.dao.goodwillghservice.security.repository;

import com.wangkang.goodwillghservice.dao.goodwillghservice.security.model.PermissionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface PermissionGroupRepository extends JpaRepository<PermissionGroup, UUID> {

    Boolean existsPermissionGroupByName(String name);

    PermissionGroup findByName(String name);

    List<PermissionGroup> findByNameIn(Collection<String> names);
}
