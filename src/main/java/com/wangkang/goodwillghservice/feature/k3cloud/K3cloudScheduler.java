package com.wangkang.goodwillghservice.feature.k3cloud;

import com.wangkang.goodwillghservice.feature.audit.entity.SystemAuthenticated;
import com.wangkang.goodwillghservice.feature.k3cloud.service.K3cloudOrderService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class K3cloudScheduler {


    private static final Log log = LogFactory.getLog(K3cloudScheduler.class);
    private final K3cloudOrderService k3cloudOrderService;
    private final K3cloudSyncExecutor k3cloudSyncExecutor;

    // 默认覆盖时间范围，单位秒
    private static final int DEFAULT_OVERLAP = 90;
    // 最长覆盖时间范围
    private static final long MAX_OVERLAP = 3600; // 1 hour
    // 失败次数，用于下一次同步任务补偿时间范围
    private int failedTimes = 0;

    public K3cloudScheduler(K3cloudOrderService k3cloudOrderService, K3cloudSyncExecutor k3cloudSyncExecutor) {
        this.k3cloudOrderService = k3cloudOrderService;
        this.k3cloudSyncExecutor = k3cloudSyncExecutor;
    }

    @SystemAuthenticated
    @Scheduled(cron = "0 * * * * *", zone = "UTC")
    public void syncK3CloudOrder() {
        log.info("Started to synchronize k3 cloud order");
        long overlap = Math.min((long) (1 + failedTimes) * DEFAULT_OVERLAP, MAX_OVERLAP);

        Optional<Integer> result;
        try {
            result = k3cloudSyncExecutor.tryExecuteOnce(() -> k3cloudOrderService.syncModifiedOrder(overlap));
        } catch (Exception e) {
            // 已拿到锁，但业务执行失败
            failedTimes++;
            log.warn("K3 cloud sync failed, overlap=" + overlap, e);
            return;
        }
        if (result.isEmpty()) {
            // 没拿到锁
            failedTimes++;
            log.info("Sync skipped due to lock contention, failedTimes=" + failedTimes);
            return;
        }

        // 成功执行
        int synced = result.get();
        log.info("Sync finished, synced rows=" + synced);

        // 成功一次必须归零
        failedTimes = 0;
    }

    @SystemAuthenticated
    @Scheduled(cron = "0 10 * * * *", zone = "UTC")
    public void syncK3CloudDeletedOrder(){
        k3cloudOrderService.syncDeletedOrder();
    }
}
