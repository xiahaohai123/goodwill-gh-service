package com.wangkang.goodwillghservice.feature.k3cloud;


import com.wangkang.goodwillghservice.feature.k3cloud.model.K3SyncResult;
import com.wangkang.goodwillghservice.feature.k3cloud.service.K3cloudOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/k3cloud/order")
public class K3cloudOrderController {

    private final K3cloudOrderService k3cloudOrderService;
    private final K3cloudSyncExecutor k3cloudSyncExecutor;

    public K3cloudOrderController(K3cloudOrderService k3cloudOrderService, K3cloudSyncExecutor k3cloudSyncExecutor) {
        this.k3cloudOrderService = k3cloudOrderService;
        this.k3cloudSyncExecutor = k3cloudSyncExecutor;
    }

    /**
     * 同步修改的订单
     * @param overlap 同步最后修改时间为 T - overlap (s) 的订单
     * @return 同步行数
     */
    @PreAuthorize("hasAnyAuthority('DEBUG')")
    @PutMapping("/{overlap}")
    public ResponseEntity<Object> syncModifiedOrder(@PathVariable long overlap) {
        Optional<K3SyncResult> result;

        try {
            result = k3cloudSyncExecutor.executeWithRetry(
                    () -> k3cloudOrderService.syncModifiedOrderAndAudit(overlap),
                    3,          // 最多重试 3 次
                    1000        // 每次最多等待 1 秒
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Sync interrupted");
        } catch (Exception e) {
            // 已拿到锁，但业务执行失败
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Sync failed: " + e.getMessage());
        }

        // 成功执行
        return result.<ResponseEntity<Object>>map(ResponseEntity::ok)
                // 锁竞争失败（不是异常）
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Another sync task is running, please retry later"));
    }

    @PreAuthorize("hasAnyAuthority('DEBUG')")
    @DeleteMapping
    public ResponseEntity<Object> syncDeletedOrder() {
        int i = k3cloudOrderService.syncDeletedOrderAndAudit();
        return ResponseEntity.ok(i);
    }
}
