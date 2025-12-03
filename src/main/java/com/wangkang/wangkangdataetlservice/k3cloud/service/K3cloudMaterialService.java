package com.wangkang.wangkangdataetlservice.k3cloud.service;

import com.wangkang.wangkangdataetlservice.dao.wkgwsales.model.FinalGoodsSnapshotPO;
import com.wangkang.wangkangdataetlservice.dao.wkgwsales.model.InventoryPO;

import java.util.List;

public interface K3cloudMaterialService {

    List<InventoryPO> getMaterial4BI();
    List<InventoryPO> getMaterial4BI(int startIndex, int limit);

    List<FinalGoodsSnapshotPO> fetchFinalGoods4BI();
}
