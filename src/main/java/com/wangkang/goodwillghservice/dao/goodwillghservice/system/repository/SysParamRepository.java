package com.wangkang.goodwillghservice.dao.goodwillghservice.system.repository;

import com.wangkang.goodwillghservice.dao.goodwillghservice.system.model.SysParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SysParamRepository extends JpaRepository<SysParam, UUID>,
        JpaSpecificationExecutor<SysParam> {
    Optional<SysParam> findByParamKey(String paramKey);
}
