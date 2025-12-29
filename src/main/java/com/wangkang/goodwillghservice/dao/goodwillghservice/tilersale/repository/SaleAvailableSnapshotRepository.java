package com.wangkang.goodwillghservice.dao.goodwillghservice.tilersale.repository;

import com.wangkang.goodwillghservice.dao.goodwillghservice.tilersale.model.SaleAvailableSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SaleAvailableSnapshotRepository extends JpaRepository<SaleAvailableSnapshot, UUID>,
        JpaSpecificationExecutor<SaleAvailableSnapshot> {

}
