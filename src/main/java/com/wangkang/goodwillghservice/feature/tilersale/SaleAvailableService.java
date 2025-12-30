package com.wangkang.goodwillghservice.feature.tilersale;

import java.util.Collection;
import java.util.UUID;

public interface SaleAvailableService {

    /**
     * 为所有经销商构建全量纠偏快照
     */
    void buildFullSnapshot4AllDistributor();

    /**
     * 为某个经销商构建全量纠偏快照
     * @param distributorId 经销商用户 id
     */
    void buildFullSnapshot4Distributor(UUID distributorId);

    /** 为所有经销商构建增量快照 */
    void buildIncrementalSnapshot4AllDistributor();

    /**
     * 为某个经销商构建增量快照
     * @param distributorId 经销商用户 id
     */
    void buildIncrementalSnapshot4Distributor(UUID distributorId);

    Collection<SaleAvailableDTO> getSaleAvailable(UUID distributorId);
}
