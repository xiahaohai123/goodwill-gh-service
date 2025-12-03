package com.wangkang.wangkangdataetlservice.audit;

import com.wangkang.wangkangdataetlservice.audit.entity.LoginLogFilterDTO;
import com.wangkang.wangkangdataetlservice.audit.entity.OperationLogFilterDTO;
import com.wangkang.wangkangdataetlservice.dao.dataetlservice.audit.model.LoginLog;
import com.wangkang.wangkangdataetlservice.dao.dataetlservice.audit.model.OperationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ActionLogService {
    Page<OperationLog> getOperationLogs(OperationLogFilterDTO operationLogFilterDTO, Pageable pageable);

    Page<LoginLog> getLoginLogs(LoginLogFilterDTO loginLogFilterDTO, Pageable pageable);
}
