package com.wangkang.goodwillghservice.feature.gwtoolstation;

import com.wangkang.goodwillghservice.feature.audit.entity.SystemAuthenticated;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TileScheduler {


    private static final Log log = LogFactory.getLog(TileScheduler.class);
    private final TileService tileService;

    public TileScheduler(TileService tileService) {
        this.tileService = tileService;
    }

    /**
     * 更新金蝶云数据到工作站
     */
    @SystemAuthenticated
    @Scheduled(cron = "0 0 4 * * *", zone = "UTC")
    public void updateK3CloudData2Station() {
        log.info("Started to update tile data from goodwill-tool-station");
        tileService.updateAllTile();
    }
}
