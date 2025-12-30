package com.wangkang.goodwillghservice.dao.goodwillghservice.tilersale.repository;

import com.wangkang.goodwillghservice.dao.goodwillghservice.tilersale.model.SaleAvailableSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SaleAvailableSnapshotRepository extends JpaRepository<SaleAvailableSnapshot, UUID>,
        JpaSpecificationExecutor<SaleAvailableSnapshot> {
    @Query("""
                SELECT ss
                FROM SaleAvailableSnapshot ss
                WHERE ss.distributorId = :distributorId
                  AND ss.batchId = (
                      SELECT s2.batchId
                      FROM SaleAvailableSnapshot s2
                      WHERE s2.distributorId = :distributorId
                      ORDER BY s2.createdAt DESC
                      LIMIT 1
                  )
            """)
    List<SaleAvailableSnapshot> findLatestBatchByDistributorId(@Param("distributorId") UUID distributorId);
}
