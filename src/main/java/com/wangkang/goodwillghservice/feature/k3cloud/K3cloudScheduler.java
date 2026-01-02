package com.wangkang.goodwillghservice.feature.k3cloud;

import com.wangkang.goodwillghservice.core.SysParamService;
import com.wangkang.goodwillghservice.feature.audit.entity.SystemAuthenticated;
import com.wangkang.goodwillghservice.feature.k3cloud.model.K3SyncResult;
import com.wangkang.goodwillghservice.feature.k3cloud.service.K3cloudOrderService;
import com.wangkang.goodwillghservice.share.util.DateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Optional;

@Component
public class K3cloudScheduler {


    private static final Log log = LogFactory.getLog(K3cloudScheduler.class);
    private final K3cloudOrderService k3cloudOrderService;
    private final K3cloudSyncExecutor k3cloudSyncExecutor;

    private static final String SYNC_KEY = "K3CLOUD_LAST_SYNC_TIME";
    private final SysParamService sysParamService;
    // 冗余缓冲时间（秒），处理金蝶系统事务提交延迟导致的漏单
    private static final int BUFFER_SECONDS = 60;

    public K3cloudScheduler(K3cloudOrderService k3cloudOrderService, K3cloudSyncExecutor k3cloudSyncExecutor,
                            SysParamService sysParamService) {
        this.k3cloudOrderService = k3cloudOrderService;
        this.k3cloudSyncExecutor = k3cloudSyncExecutor;
        this.sysParamService = sysParamService;
    }

    @SystemAuthenticated
    @Scheduled(cron = "0 * * * * *", zone = "UTC")
    public void syncK3CloudOrder() {
        log.info("Started to synchronize k3 cloud order");
        // 1. 获取上次同步成功的终点（从数据库读取）
        OffsetDateTime lastSyncTime = sysParamService.getOffsetDateTime(SYNC_KEY);

        // 2. 确定本次同步的物理终点（取当前时间且需要使用 CST 时间）
        OffsetDateTime currentTime = DateUtil.currentOffsetDateTimeUTC();

        // 3. 计算实际查询范围（带上冗余 buffer）
        // 哪怕上次同步到 10:00，这次我们从 09:59 开始查，确保边缘数据不丢失
        OffsetDateTime searchFrom = lastSyncTime.minusSeconds(BUFFER_SECONDS);

        Optional<K3SyncResult> result;
        try {
            result = k3cloudSyncExecutor.tryExecuteOnce(() ->
                    k3cloudOrderService.syncModifiedOrder(searchFrom, currentTime)
            );
        } catch (Exception e) {
            // 已拿到锁，但业务执行失败：数据库不更新，下次执行依然从当前的 lastSyncTime 开始
            log.error("K3 cloud sync failed, window: [" + searchFrom + " TO " + currentTime + "]", e);
            return;
        }

        if (result.isPresent()) {
            // 4. 执行成功
            K3SyncResult k3SyncResult = result.get();
            log.info(
                    "Sync finished, synced rows=" + k3SyncResult.savedRows() + ". Updating baseline to: " + currentTime);
            // 5. 只有成功后才更新数据库基线
            sysParamService.updateParam(SYNC_KEY, currentTime);
        } else {
            // 没拿到锁（其他节点在跑）
            log.info("Sync skipped due to lock contention.");
        }
    }

    @SystemAuthenticated
    @Scheduled(cron = "0 10 * * * *", zone = "UTC")
    public void syncK3CloudDeletedOrder() {
        k3cloudOrderService.syncDeletedOrder();
    }
}
