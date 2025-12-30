package com.wangkang.goodwillghservice.feature.tilersale;

import com.wangkang.goodwillghservice.dao.goodwillghservice.tilersale.model.SaleAvailableSnapshot;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface SaleAvailableService {

    /**
     * 为所有经销商构建全量纠偏快照
     */
    void takeFullSnapshot4AllDistributor();


    List<SaleAvailableSnapshot> getLatestSnapshotByDistributor(UUID distributorId);

    Collection<SaleAvailableDTO> getSaleAvailable(UUID distributorId);
}
