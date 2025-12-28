package com.wangkang.goodwillghservice.feature.k3cloud.service;

import com.wangkang.goodwillghservice.dao.goodwillghservice.order.model.K3SaleOrder;
import com.wangkang.goodwillghservice.dao.goodwillghservice.order.repository.K3SaleOrderRepository;
import com.wangkang.goodwillghservice.feature.audit.entity.ActionType;
import com.wangkang.goodwillghservice.feature.audit.entity.Auditable;
import com.wangkang.goodwillghservice.feature.k3cloud.model.OrderBillType;
import com.wangkang.goodwillghservice.feature.k3cloud.model.OrderCloseStatus;
import com.wangkang.goodwillghservice.feature.k3cloud.model.OrderDocumentStatus;
import com.wangkang.goodwillghservice.feature.k3cloud.model.OrderVerifyStatus;
import com.wangkang.goodwillghservice.share.util.ChoreUtil;
import com.wangkang.goodwillghservice.share.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class K3cloudOrderServiceImpl implements K3cloudOrderService {
    private static final String K3_ORDER_FIELD_BILL_NUMBER = "FBillNo";

    private static final String BASE_ORDER_FILTER = "FUnitID.FNumber = 'CTN'" +
            " and FBillTypeId.FNumber = '" + OrderBillType.STANDARD.code + "'";

    /** 同步用的订单列 */
    private static final String ORDER_FIELD = "FBillNo, FCustId, FCustId.FNumber, FCustId.FName, FCreateDate, FDocumentStatus," +
            "FMaterialId.FNumber, FUnitID.FNumber, FQty, FReviewStatus, FReviewDate, FCloseStatus, FCloseDate, FModifyDate," +
            "FStockBaseOutJoinQty";
    private static final Log log = LogFactory.getLog(K3cloudOrderServiceImpl.class);
    private final K3cloudRequestService k3cloudRequestService;
    private final K3cloudOrderSyncService k3cloudOrderSyncService;
    private final K3SaleOrderRepository k3SaleOrderRepository;

    public K3cloudOrderServiceImpl(K3cloudRequestService k3cloudRequestService,
                                   K3cloudOrderSyncService k3cloudOrderSyncService,
                                   K3SaleOrderRepository k3SaleOrderRepository) {
        this.k3cloudRequestService = k3cloudRequestService;
        this.k3cloudOrderSyncService = k3cloudOrderSyncService;
        this.k3SaleOrderRepository = k3SaleOrderRepository;
    }

    @Override
    public int syncModifiedOrder(long overlap) {
        return doSyncModifiedOrder(overlap);
    }

    @Auditable(actionType = ActionType.K3_ORDER, actionName = "Synchronize k3 order")
    @Override
    public int syncModifiedOrderAndAudit(long overlap) {
        return doSyncModifiedOrder(overlap);
    }

    private int doSyncModifiedOrder(long overlap) {
        // 循环更新数据
        int startIndex = 0;
        int limit = 2000;
        Collection<K3SaleOrder> page;
        int updatedRows = 0;
        do {
            page = getOrderByLastModifiedDateFrom(overlap, startIndex, limit);
            if (!page.isEmpty()) {
                int savedRows = k3cloudOrderSyncService.syncK3OrderPage(page);// 分页级事务
                updatedRows += savedRows;
            }
            startIndex += limit;
        } while (page.size() == limit);

        log.info("[Sync Summary]: Updated " + updatedRows + " rows for k3 sale order");
        return updatedRows;
    }

    private List<K3SaleOrder> getOrderByLastModifiedDateFrom(long overlap, int startIndex, int limit) {
        String currentFilterString = BASE_ORDER_FILTER;
        String endTime = DateUtil.utcNowMinusSecondsToBeijingFormatted(overlap);

        currentFilterString += " and FModifyDate >= '" + endTime + "'";
        List<Map<String, Object>> orderMapList;
        orderMapList = k3cloudRequestService.billQueryOrderFieldsByFilter(currentFilterString, ORDER_FIELD, startIndex,
                limit);
        return map2K3SaleOrder(orderMapList);
    }

    private static List<K3SaleOrder> map2K3SaleOrder(List<Map<String, Object>> orderMapList) {
        // FModifyDate, FCloseDate, FCreateDate 返回的是北京时间，需要调整为 UTC 时间再存储
        return orderMapList.stream().map(obj -> {
            K3SaleOrder po = new K3SaleOrder();
            po.setBillNo(ChoreUtil.toString(obj.get(K3_ORDER_FIELD_BILL_NUMBER)));
            po.setCustomerId(ChoreUtil.toInteger(obj.get("FCustId")));
            po.setCustomerNumber(ChoreUtil.toString(obj.get("FCustId.FNumber")));
            po.setCustomerName(ChoreUtil.toString(obj.get("FCustId.FName")));
            String createDateString = ChoreUtil.toString(obj.get("FCreateDate"));
            OffsetDateTime createDate = DateUtil.parseISOFromZone(createDateString, ZoneOffset.ofHours(8));
            po.setCreatedDate(createDate);
            po.setDocumentStatus(OrderDocumentStatus.fromCode(ChoreUtil.toString(obj.get("FDocumentStatus"))));
            po.setMaterialNumber(ChoreUtil.toString(obj.get("FMaterialId.FNumber")));
            po.setUnit(ChoreUtil.toString(obj.get("FUnitID.FNumber")));
            po.setQuantity(new BigDecimal(ChoreUtil.toString(obj.get("FQty"))).intValue());
            po.setVerifyStatus(OrderVerifyStatus.fromCode(ChoreUtil.toString(obj.get("FReviewStatus"))));
            String verifyDateString = ChoreUtil.toString(obj.get("FReviewDate"));
            if (StringUtils.isNotBlank(verifyDateString)) {
                OffsetDateTime verifyDate = DateUtil.parseISOFromZone(verifyDateString, ZoneOffset.ofHours(8));
                po.setVerifyDate(verifyDate);
            }
            po.setCloseStatus(OrderCloseStatus.fromCode(ChoreUtil.toString(obj.get("FCloseStatus"))));
            String closeDateString = ChoreUtil.toString(obj.get("FCloseDate"));
            if (StringUtils.isNotBlank(closeDateString)) {
                OffsetDateTime closeDate = DateUtil.parseISOFromZone(closeDateString, ZoneOffset.ofHours(8));
                po.setCloseDate(closeDate);
            }
            String modifyDateStr = ChoreUtil.toString(obj.get("FModifyDate"));
            OffsetDateTime modifyDate = DateUtil.parseISOFromZone(modifyDateStr, ZoneOffset.ofHours(8));
            po.setLastModifyDate(modifyDate);
            return po;
        }).toList();
    }

    @Transactional
    @Override
    public int syncDeletedOrder() {
        return doSyncDeletedOrder();
    }


    @Auditable(actionType = ActionType.K3_ORDER, actionName = "Synchronize k3 deleted order")
    @Transactional
    @Override
    public int syncDeletedOrderAndAudit() {
        return doSyncDeletedOrder();
    }

    private int doSyncDeletedOrder() {
        List<String> billNumbers = k3SaleOrderRepository.findDistinctBillNoByCloseStatus(OrderCloseStatus.NORMAL);
        if (CollectionUtils.isEmpty(billNumbers)) {
            return 0;
        }
        // 构造 IN 子句：'SO001','SO002',...
        String inClause = billNumbers.stream()
                .map(no -> "'" + no + "'")
                .collect(Collectors.joining(","));
        String filter = K3_ORDER_FIELD_BILL_NUMBER + " IN (" + inClause + ")";
        List<Map<String, Object>> orderMapList = k3cloudRequestService.billQueryOrderFieldsByFilter(filter,
                K3_ORDER_FIELD_BILL_NUMBER);
        Set<String> k3BillNoSet = orderMapList.stream()
                .map(item -> ChoreUtil.toString(item.get(K3_ORDER_FIELD_BILL_NUMBER)))
                .collect(Collectors.toSet());

        // 本地订单号集合
        Set<String> localBillNoSet = new HashSet<>(billNumbers);
        // 差集：本地有，但金蝶云没有
        localBillNoSet.removeAll(k3BillNoSet);
        // localBillNoSet 即为“已在金蝶云被删除的订单号”
        if (!localBillNoSet.isEmpty()) {
            log.info("[Sync Deleted Orders] Found deleted orders in K3: " + localBillNoSet);
            int deletedRows = k3SaleOrderRepository.deleteByBillNoIn(localBillNoSet);
            log.info("[Sync Deleted Orders] Deleted " + deletedRows + " rows");
            return deletedRows;
        }
        return 0;
    }
}
