package com.wangkang.wangkangdataetlservice.dao.dataetlservice.audit.repository;

import com.wangkang.wangkangdataetlservice.dao.dataetlservice.audit.model.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OperationLogRepository extends JpaRepository<OperationLog, UUID>,
        JpaSpecificationExecutor<OperationLog> {
}
