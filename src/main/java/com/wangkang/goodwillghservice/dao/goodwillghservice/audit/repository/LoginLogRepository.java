package com.wangkang.goodwillghservice.dao.goodwillghservice.audit.repository;

import com.wangkang.goodwillghservice.dao.goodwillghservice.audit.model.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LoginLogRepository extends JpaRepository<LoginLog, UUID>,
        JpaSpecificationExecutor<LoginLog> {
}
