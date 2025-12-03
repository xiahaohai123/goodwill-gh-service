package com.wangkang.wangkangdataetlservice.dao.wkgwsales.repository;

import com.wangkang.wangkangdataetlservice.dao.wkgwsales.model.FinalGoodsSnapshotPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.UUID;

@Repository
public interface FinalGoodsSnapshotRepository extends JpaRepository<FinalGoodsSnapshotPO, UUID> {

    void deleteByCreatedAtAfter(OffsetDateTime createdAtAfter);
}
