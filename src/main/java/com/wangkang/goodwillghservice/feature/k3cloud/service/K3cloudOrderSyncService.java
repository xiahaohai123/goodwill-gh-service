package com.wangkang.goodwillghservice.feature.k3cloud.service;

import com.wangkang.goodwillghservice.dao.goodwillghservice.order.model.K3SaleOrder;
import com.wangkang.goodwillghservice.dao.goodwillghservice.order.repository.K3SaleOrderRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

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
    public int syncK3OrderPage(Collection<K3SaleOrder> orders) {
        if (CollectionUtils.isEmpty(orders)) {
            return 0;
        }
        List<String> billNos = orders.stream()
                .map(K3SaleOrder::getBillNo)
                .toList();
        k3SaleOrderRepository.deleteByBillNoIn(billNos);

        List<K3SaleOrder> savedOrders = k3SaleOrderRepository.saveAll(orders);

        int deleteRows = billNos.size();
        log.info("Deleted " + deleteRows + " rows k3 orders");
        int savedRows = savedOrders.size();
        log.info("Saved " + savedRows + " rows k3 orders");
        return savedRows;
    }
}
