package com.wangkang.goodwillghservice.feature.k3cloud.service;

import com.wangkang.goodwillghservice.dao.goodwillghservice.order.model.K3SaleOrder;
import com.wangkang.goodwillghservice.dao.goodwillghservice.order.repository.K3SaleOrderRepository;
import com.wangkang.goodwillghservice.feature.k3cloud.model.K3SyncResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 为了确保分页事务注解生效，分出来的子类，方便 spring 代理
 */
@Service
public class K3cloudOrderSyncService {

    private static final Log log = LogFactory.getLog(K3cloudOrderSyncService.class);
    private final K3SaleOrderRepository k3SaleOrderRepository;

    public K3cloudOrderSyncService(K3SaleOrderRepository k3SaleOrderRepository) {
        this.k3SaleOrderRepository = k3SaleOrderRepository;
    }

    /**
     * upsert 保存一页 来自 金蝶云 的订单数据
     * @param orders 订单数据
     * @return 保存的行数
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public K3SyncResult syncK3OrderPage(Collection<K3SaleOrder> orders, Set<String> processedBillNos) {
        if (CollectionUtils.isEmpty(orders)) {
            return new K3SyncResult(0, 0);
        }
        List<String> billNosToClear = orders.stream()
                .map(K3SaleOrder::getBillNo)
                .distinct()
                .filter(no -> !processedBillNos.contains(no)) // 过滤掉本轮已经删过的
                .toList();
        // 执行删除，并加入已处理标记
        int deletedRows = 0;
        if (!billNosToClear.isEmpty()) {
            deletedRows = k3SaleOrderRepository.deleteByBillNoIn(billNosToClear);
            processedBillNos.addAll(billNosToClear);
            log.info("Cleared " + billNosToClear.size() + " existing orders from DB to prepare for fresh sync");
        }

        List<K3SaleOrder> savedOrders = k3SaleOrderRepository.saveAll(orders);

        log.info("Deleted " + deletedRows + " rows k3 orders");
        int savedRows = savedOrders.size();
        log.info("Saved " + savedRows + " rows k3 orders");
        return new K3SyncResult(deletedRows, savedRows);
    }
}
