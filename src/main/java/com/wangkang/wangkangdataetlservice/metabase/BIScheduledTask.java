package com.wangkang.wangkangdataetlservice.metabase;

import com.wangkang.wangkangdataetlservice.audit.entity.SystemAuthenticated;
import com.wangkang.wangkangdataetlservice.util.DateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneOffset;

@Component
public class BIScheduledTask {

    private static final Log log = LogFactory.getLog(BIScheduledTask.class);
    private final BIBusinessService biBusinessService;

    public BIScheduledTask(BIBusinessService biBusinessService) {
        this.biBusinessService = biBusinessService;
    }

    @SystemAuthenticated
    @Scheduled(cron = "0 0 6,12,18 * * *", zone = "UTC")
    public void fetchInventory4BI() {
        log.info("Start to do scheduled task: fetchInventory4BI. Execute Time: " + DateUtil.currentUTCTimeWithZone());
        biBusinessService.updateInventory();
    }

    @SystemAuthenticated
    @Scheduled(cron = "0 0 * * * *", zone = "UTC")
    public void snapshotFinalGoods4BI() {
        log.info("Start to do scheduled task: snapshotFinalGoods4BI. Execute Time: " + DateUtil.currentUTCTimeWithZone());
        biBusinessService.snapshotFinalGoods();
    }


    @SystemAuthenticated
    @Scheduled(cron = "0 25 18 * * *", zone = "UTC")
    public void fetchOrder4BIDaily18() {
        LocalDate endDate = LocalDate.now(ZoneOffset.UTC).plusDays(1);
        LocalDate startDate = endDate.minusDays(3);
        updateOrder2BIBefore(startDate, endDate);
    }

    @SystemAuthenticated
    @Scheduled(cron = "0 0 6 * * *", zone = "UTC")
    public void fetchOrder4BIDaily03() {
        LocalDate endDate = LocalDate.now(ZoneOffset.UTC);
        LocalDate startDate = endDate.minusDays(3);
        updateOrder2BIBefore(startDate, endDate);
    }

    @SystemAuthenticated
    @Scheduled(cron = "0 0 7 ? * SUN", zone = "UTC")
    public void fetchOrder4BIWeeklySunday() {
        LocalDate endDate = LocalDate.now(ZoneOffset.UTC);
        LocalDate startDate = endDate.minusDays(7);
        updateOrder2BIBefore(startDate, endDate);
    }

    @SystemAuthenticated
    @Scheduled(cron = "0 0 4 ? * MON", zone = "UTC")
    public void fetchOrder4BIWeeklyMon() {
        LocalDate endDate = LocalDate.now(ZoneOffset.UTC).minusDays(1);
        LocalDate startDate = endDate.minusDays(7);
        updateOrder2BIBefore(startDate, endDate);
    }

    @SystemAuthenticated
    @Scheduled(cron = "0 0 4 1,2,3,4,5,6 * ?", zone = "UTC")
    public void fetchOrderMonthlyEarly() {
        LocalDate endDate = LocalDate.now(ZoneOffset.UTC).withDayOfMonth(1);
        // 上个月1号
        LocalDate startDate = LocalDate.now(ZoneOffset.UTC)
                .minusMonths(1)
                .withDayOfMonth(1);

        // 按周切分执行
        while (!startDate.isEqual(endDate)) {
            // 每次取一周的时间区间
            LocalDate nextWeek = startDate.plusWeeks(1);
            if (nextWeek.isAfter(endDate)) {
                nextWeek = endDate;
            }
            updateOrder2BIBefore(startDate, nextWeek);
            // 更新 startDate 为下一个区间的开始
            startDate = nextWeek;
        }
    }

    private void updateOrder2BIBefore(LocalDate startDate, LocalDate endDate) {
        String endDateTime = DateUtil.formatTime2YMD(endDate);
        String startDateTime = DateUtil.formatTime2YMD(startDate);
        log.info("Start to do scheduled task: updateOrder. Execute Time: " + DateUtil.currentUTCTimeWithZone());
        log.info("update order data fetched from " + startDate + " to " + endDateTime);
        try {
            biBusinessService.updateOrder2BI(startDateTime, endDateTime);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
