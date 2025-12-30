package com.wangkang.goodwillghservice.feature.tilersale;

import com.wangkang.goodwillghservice.feature.audit.entity.SystemAuthenticated;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SaleScheduler {

    private static final Log log = LogFactory.getLog(SaleScheduler.class);
    private final SaleAvailableService saleAvailableService;

    public SaleScheduler(SaleAvailableService saleAvailableService) {
        this.saleAvailableService = saleAvailableService;
    }

    @SystemAuthenticated
    @Scheduled(cron = "0 0 0 * * *", zone = "UTC")
    public void buildIncrementalSnapshot() {
        log.info("Started to build incremental snapshot");
        saleAvailableService.buildIncrementalSnapshot4AllDistributor();
    }
}
