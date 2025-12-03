package com.wangkang.wangkangdataetlservice.dao.wkgwsales.repository;

import com.wangkang.wangkangdataetlservice.dao.wkgwsales.model.InventoryPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryPO, UUID> {

    List<InventoryPO> findAllBySkuCodeIn(Collection<String> skuCodes);
}
