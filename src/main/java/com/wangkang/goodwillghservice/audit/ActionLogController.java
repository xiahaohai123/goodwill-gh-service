package com.wangkang.goodwillghservice.audit;


import com.wangkang.goodwillghservice.audit.entity.LoginLogFilterDTO;
import com.wangkang.goodwillghservice.audit.entity.OperationLogFilterDTO;
import com.wangkang.goodwillghservice.dao.goodwillghservice.audit.model.LoginLog;
import com.wangkang.goodwillghservice.dao.goodwillghservice.audit.model.OperationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 巡检任务 */
@PreAuthorize("hasAnyAuthority('ADMIN')") // 仅允许 ADMIN 角色访问
@RestController
@RequestMapping("/api/log/action")
public class ActionLogController {

    private final ActionLogService actionLogService;

    public ActionLogController(ActionLogService actionLogService) {
        this.actionLogService = actionLogService;
    }


    @GetMapping("/operation")
    public ResponseEntity<Page<OperationLog>> getOperations(
            @ModelAttribute OperationLogFilterDTO operationLogFilterDTO,
            Pageable pageable) {
        Page<OperationLog> operationLogs = actionLogService.getOperationLogs(operationLogFilterDTO, pageable);
        return ResponseEntity.ok(operationLogs);
    }

    @GetMapping("/login")
    public ResponseEntity<Page<LoginLog>> getLogin(
            @ModelAttribute LoginLogFilterDTO loginLogFilterDTO, Pageable pageable) {
        Page<LoginLog> loginLogs = actionLogService.getLoginLogs(loginLogFilterDTO, pageable);
        return ResponseEntity.ok(loginLogs);
    }
}
