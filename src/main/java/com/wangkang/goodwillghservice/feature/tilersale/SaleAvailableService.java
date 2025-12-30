package com.wangkang.goodwillghservice.feature.tilersale;

import java.util.Collection;
import java.util.UUID;

public interface SaleAvailableService {

    /**
     * 为所有经销商构建全量纠偏快照
     */
    void takeFullSnapshot4AllDistributor();

    /**
     * 为某个经销商构建全量纠偏快照
     * @param distributorId 经销商用户 id
     */
    void takeFullSnapshot4Distributor(UUID distributorId);

    Collection<SaleAvailableDTO> getSaleAvailable(UUID distributorId);
}
