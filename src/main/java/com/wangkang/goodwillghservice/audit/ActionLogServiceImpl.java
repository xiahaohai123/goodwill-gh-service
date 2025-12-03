package com.wangkang.goodwillghservice.audit;

import com.wangkang.goodwillghservice.audit.entity.LoginLogFilterDTO;
import com.wangkang.goodwillghservice.audit.entity.OperationLogFilterDTO;
import com.wangkang.goodwillghservice.dao.goodwillghservice.audit.filter.OperationLogSpecification;
import com.wangkang.goodwillghservice.dao.goodwillghservice.audit.model.LoginLog;
import com.wangkang.goodwillghservice.dao.goodwillghservice.audit.model.OperationLog;
import com.wangkang.goodwillghservice.dao.goodwillghservice.audit.repository.LoginLogRepository;
import com.wangkang.goodwillghservice.dao.goodwillghservice.audit.repository.OperationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ActionLogServiceImpl implements ActionLogService {

    private final OperationLogRepository operationLogRepository;
    private final LoginLogRepository loginLogRepository;

    @Autowired
    public ActionLogServiceImpl(OperationLogRepository operationLogRepository,
                                LoginLogRepository loginLogRepository) {
        this.operationLogRepository = operationLogRepository;
        this.loginLogRepository = loginLogRepository;
    }


    @Override
    public Page<OperationLog> getOperationLogs(OperationLogFilterDTO operationLogFilterDTO, Pageable pageable) {
        Specification<OperationLog> spec = OperationLogSpecification.filterBy(operationLogFilterDTO);
        return operationLogRepository.findAll(spec, pageable);
    }

    @Override
    public Page<LoginLog> getLoginLogs(LoginLogFilterDTO loginLogFilterDTO, Pageable pageable) {
        return loginLogRepository.findAll(pageable);
    }
}
