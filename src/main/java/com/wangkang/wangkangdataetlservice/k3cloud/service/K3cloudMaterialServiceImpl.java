package com.wangkang.wangkangdataetlservice.k3cloud.service;

import com.wangkang.wangkangdataetlservice.dao.wkgwsales.model.FinalGoodsSnapshotPO;
import com.wangkang.wangkangdataetlservice.dao.wkgwsales.model.InventoryPO;
import com.wangkang.wangkangdataetlservice.util.ChoreUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
public class K3cloudMaterialServiceImpl implements K3cloudMaterialService {
    private static final Log log = LogFactory.getLog(K3cloudMaterialServiceImpl.class);
    private final K3cloudRequestService k3cloudRequestService;

    @Autowired
    public K3cloudMaterialServiceImpl(K3cloudRequestService k3cloudRequestService) {
        this.k3cloudRequestService = k3cloudRequestService;
    }


    @Override
    public List<InventoryPO> getMaterial4BI() {
        List<InventoryPO> inventoryList;
        int startIndex = 0;
        int limit = 2000;
        List<InventoryPO> resultList = new ArrayList<>();
        do {
            inventoryList =getMaterial4BI(startIndex, limit);
            log.debug("inventory list: " + inventoryList);
            resultList.addAll(inventoryList);
            startIndex += limit;
        } while (inventoryList.size() == limit);
        log.info("fetched materialMapList size: " + resultList.size());
        return resultList;
    }

    @Override
    public List<InventoryPO> getMaterial4BI(int startIndex, int limit) {
        String filterString = "FStoreUnitID.FNumber = 'CTN'";
        String fieldKeys = "FUseOrgId.FNumber, FNumber, FName, FSpecification, FDocumentStatus, " +
                "FForbidStatus, FErpClsID, FStoreUnitID.FNumber, F_ora_CanQty, F_ora_AllQty, FCreateDate ";
        List<Map<String, Object>> materialMapList;
        materialMapList = k3cloudRequestService.billQueryMaterialFieldsByFilter(filterString, fieldKeys, startIndex,
                limit);
        List<InventoryPO> inventoryList = materialMapList.stream().map(obj -> {
            InventoryPO po = new InventoryPO();
            po.setOrganization(Objects.toString(obj.get("FUseOrgId.FNumber")));
            po.setSkuCode(Objects.toString(obj.get("FNumber")));
            po.setName(Objects.toString(obj.get("FName")));
            po.setModel(Objects.toString(obj.get("FSpecification")));
            po.setDataStatus(Objects.toString(obj.get("FDocumentStatus")));
            po.setBanStatus(Objects.toString(obj.get("FForbidStatus")));
            po.setMaterialProperties(Objects.toString(obj.get("FErpClsID")));
            po.setInventoryUnit(Objects.toString(obj.get("FStoreUnitID.FNumber")));
            po.setInventoryAvailable(new BigDecimal(Objects.toString(obj.get("F_ora_CanQty"))).floatValue());
            po.setInventory(new BigDecimal(Objects.toString(obj.get("F_ora_AllQty"))).floatValue());
            po.setCreateAt(LocalDateTime.parse(ChoreUtil.toString(obj.get("FCreateDate"))));
            return po;
        }).toList();
        log.debug("inventory list: " + inventoryList);
        return new ArrayList<>(inventoryList);
    }

    @Override
    public List<FinalGoodsSnapshotPO> fetchFinalGoods4BI() {
        // 无法通过 F_ora_CanQty < 100 和 F_ora_CanQty < '100' 来筛选，只能全量查询再筛选
        String filterString = "FStoreUnitID.FNumber = 'CTN' and  FNumber NOT LIKE '%A3' and FNumber NOT LIKE '%C10'";
        String fieldKeys = "FNumber, FName, FSpecification, FStoreUnitID.FNumber, F_ora_CanQty, F_ora_AllQty ";

        // 循环更新数据
        int startIndex = 0;
        int limit = 2000;
        List<FinalGoodsSnapshotPO> finalGoodsCollection = new ArrayList<>();
        List<Map<String, Object>> materialMapList;
        do {
            log.info("Fetching inventory startIndex: " + startIndex + " limit: " + limit);
            materialMapList = k3cloudRequestService.billQueryMaterialFieldsByFilter(
                    filterString,
                    fieldKeys, startIndex, limit);
            // 筛选出尾货
            List<FinalGoodsSnapshotPO> inventoryList = materialMapList.stream()
                    .map(obj -> {
                        FinalGoodsSnapshotPO po = new FinalGoodsSnapshotPO();
                        po.setMaterialCode(Objects.toString(obj.get("FNumber")));
                        po.setName(Objects.toString(obj.get("FName")));
                        po.setModel(Objects.toString(obj.get("FSpecification")));
                        po.setInventoryUnit(Objects.toString(obj.get("FStoreUnitID.FNumber")));
                        po.setInventoryAvailable(ChoreUtil.toDouble(obj.get("F_ora_CanQty")));
                        po.setInventory(ChoreUtil.toDouble(obj.get("F_ora_AllQty")));
                        return po;
                    })
                    .filter(item -> item.getInventoryAvailable() < 100 && item.getInventoryAvailable() > 0)
                    .toList();
            finalGoodsCollection.addAll(inventoryList);

            startIndex += limit;
        } while (materialMapList.size() == limit);

        return finalGoodsCollection;
    }
}
