package com.wangkang.wangkangdataetlservice.metabase;

import com.wangkang.wangkangdataetlservice.audit.entity.ActionType;
import com.wangkang.wangkangdataetlservice.audit.entity.Auditable;
import com.wangkang.wangkangdataetlservice.dao.wkgwsales.model.FinalGoodsSnapshotPO;
import com.wangkang.wangkangdataetlservice.dao.wkgwsales.model.InventoryPO;
import com.wangkang.wangkangdataetlservice.dao.wkgwsales.model.SaleOrderPO;
import com.wangkang.wangkangdataetlservice.dao.wkgwsales.repository.FinalGoodsSnapshotRepository;
import com.wangkang.wangkangdataetlservice.dao.wkgwsales.repository.InventoryRepository;
import com.wangkang.wangkangdataetlservice.dao.wkgwsales.repository.SaleOrderRepository;
import com.wangkang.wangkangdataetlservice.k3cloud.service.K3cloudMaterialService;
import com.wangkang.wangkangdataetlservice.k3cloud.service.K3cloudOrderService;
import com.wangkang.wangkangdataetlservice.metabase.sale.SaleOrder;
import com.wangkang.wangkangdataetlservice.util.DateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;

import static com.wangkang.wangkangdataetlservice.util.PersistenceUtil.updateOldData;


@Service
public class BIBusinessService {

    private static final Log log = LogFactory.getLog(BIBusinessService.class);
    private final K3cloudOrderService k3cloudOrderService;
    private final SaleOrderRepository saleOrderRepository;
    private final K3cloudMaterialService k3cloudMaterialService;
    private final InventoryRepository inventoryRepository;
    private final FinalGoodsSnapshotRepository finalGoodsSnapshotRepository;

    public BIBusinessService(K3cloudOrderService k3cloudOrderService, SaleOrderRepository saleOrderRepository,
                             K3cloudMaterialService k3cloudMaterialService, InventoryRepository inventoryRepository,
                             FinalGoodsSnapshotRepository finalGoodsSnapshotRepository) {
        this.k3cloudOrderService = k3cloudOrderService;
        this.saleOrderRepository = saleOrderRepository;
        this.k3cloudMaterialService = k3cloudMaterialService;
        this.inventoryRepository = inventoryRepository;
        this.finalGoodsSnapshotRepository = finalGoodsSnapshotRepository;
    }

    @Transactional(transactionManager = "daoTransactionManagerWKGWSales")
    @Auditable(actionType = ActionType.BI, actionName = "Update order")
    public void updateOrder2BI(String startDate, String endDate) {
        // 删除数据库内本段时间的数据
        OffsetDateTime startTime = DateUtil.parseYMD2OffsetDateTime(startDate);
        OffsetDateTime endTime = DateUtil.parseYMD2OffsetDateTime(endDate);
        Integer rowQuantity = saleOrderRepository.deleteByCreateDateGreaterThanEqualAndCreateDateLessThan(startTime,
                endTime);
        log.info("Deleted " + rowQuantity + " rows for sale order");
        // 循环更新数据
        int startIndex = 0;
        int limit = 2000;
        Collection<SaleOrder> orderBIListByTime;
        do {
            log.info("Fetching order data startIndex: " + startIndex + " limit: " + limit);
            orderBIListByTime = k3cloudOrderService.getOrderBIListByCreateDate(startDate, endDate, startIndex, limit);
            // 存储到数据库内
            List<SaleOrderPO> poList = orderBIListByTime.stream().map(dto -> {
                        SaleOrderPO po = new SaleOrderPO();
                        BeanUtils.copyProperties(dto, po);
                        po.setDate(DateUtil.parseISOFromZone(dto.getDate(), ZoneOffset.UTC));
                        po.setCreateDate(DateUtil.parseISOFromZone(dto.getCreateDate(), ZoneOffset.ofHours(8)));
                        if (dto.getVerifyDate() != null) {
                            po.setVerifyDate(DateUtil.parseISOFromZone(dto.getVerifyDate(), ZoneOffset.UTC));
                        }
                        if (dto.getCloseDate() != null) {
                            po.setCloseDate(DateUtil.parseISOFromZone(dto.getCloseDate(), ZoneOffset.ofHours(8)));
                        }
                        return po;
                    }
            ).toList();
            saleOrderRepository.saveAll(poList);

            startIndex += limit;
        } while (orderBIListByTime.size() == limit);
        log.info("Updated " + startIndex + " rows for sale order");
    }

    @Auditable(actionType = ActionType.BI, actionName = "Update inventory")
    public int updateInventory() {
        // 循环更新数据
        int startIndex = 0;
        int limit = 2000;
        int affectedRows = 0;
        List<InventoryPO> inventoryList;
        do {
            log.info("Fetching inventory startIndex: " + startIndex + " limit: " + limit);
            inventoryList = k3cloudMaterialService.getMaterial4BI(startIndex, limit);
            Collection<String> skuCodes = inventoryList.stream().map(InventoryPO::getSkuCode).toList();
            List<InventoryPO> oldInventoryList = inventoryRepository.findAllBySkuCodeIn(skuCodes);
            List<InventoryPO> updatedInventoryList = updateOldData(inventoryList, oldInventoryList,
                    InventoryPO::getSkuCode, InventoryPO::updateData);
            inventoryRepository.saveAll(updatedInventoryList);
            inventoryRepository.flush();  // 立即提交
            log.info("Save inventory: Saved batch from " + startIndex + " to " + limit);
            startIndex += limit;
            affectedRows += updatedInventoryList.size();
        } while (inventoryList.size() == limit);
        log.info("Updated " + affectedRows + " rows for inventory");
        return affectedRows;
    }

    @Transactional(transactionManager = "daoTransactionManagerWKGWSales")
    @Auditable(actionType = ActionType.BI, actionName = "Snapshot final goods")
    public int snapshotFinalGoods() {
        // 删除当天的快照
        OffsetDateTime startOfTodayUTC = DateUtil.getStartOfTodayUTC();
        finalGoodsSnapshotRepository.deleteByCreatedAtAfter(startOfTodayUTC);
        // 获取小库存数据
        List<FinalGoodsSnapshotPO> finalGoodsSnapshotPOS = k3cloudMaterialService.fetchFinalGoods4BI();
        // 存储快照
        finalGoodsSnapshotRepository.saveAll(finalGoodsSnapshotPOS);
        log.info("Made " + finalGoodsSnapshotPOS.size() + "rows snapshot for final goods");
        return finalGoodsSnapshotPOS.size();
    }
}
