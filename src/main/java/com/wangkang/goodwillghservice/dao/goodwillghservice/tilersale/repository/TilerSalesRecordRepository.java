package com.wangkang.goodwillghservice.dao.goodwillghservice.tilersale.repository;

import com.wangkang.goodwillghservice.dao.goodwillghservice.tilersale.model.TilerSalesRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TilerSalesRecordRepository extends JpaRepository<TilerSalesRecord, UUID>,
        JpaSpecificationExecutor<TilerSalesRecord> {

    Page<TilerSalesRecord> findAllByDistributorIdAndProductColor(UUID distributorId,
                                                                 String productColor,
                                                                 Pageable pageable);
}
