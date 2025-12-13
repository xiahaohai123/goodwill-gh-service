package com.wangkang.goodwillghservice.feature.user.distributor;

import com.wangkang.goodwillghservice.feature.audit.entity.SystemAuthenticated;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DistributorScheduler {

    private static final Log log = LogFactory.getLog(DistributorScheduler.class);
    private final DistributorService distributorService;

    public DistributorScheduler(DistributorService distributorService) {
        this.distributorService = distributorService;
    }

    /**
     * 更新金蝶云数据到工作站
     */
    @SystemAuthenticated
    @Scheduled(cron = "0 0 6 * * *", zone = "UTC")
    public void updateK3CloudData2Station() {
        log.info("Started to update distributor K3 Cloud Data 2 Station");
        distributorService.updateDistributorExternal();
    }
}
