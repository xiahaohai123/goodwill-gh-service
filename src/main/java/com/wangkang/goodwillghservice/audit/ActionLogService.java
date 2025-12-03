package com.wangkang.goodwillghservice.audit;

import com.wangkang.goodwillghservice.audit.entity.LoginLogFilterDTO;
import com.wangkang.goodwillghservice.audit.entity.OperationLogFilterDTO;
import com.wangkang.goodwillghservice.dao.goodwillghservice.audit.model.LoginLog;
import com.wangkang.goodwillghservice.dao.goodwillghservice.audit.model.OperationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ActionLogService {
    Page<OperationLog> getOperationLogs(OperationLogFilterDTO operationLogFilterDTO, Pageable pageable);

    Page<LoginLog> getLoginLogs(LoginLogFilterDTO loginLogFilterDTO, Pageable pageable);
}
