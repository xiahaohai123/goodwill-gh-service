package com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.repository;

import com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.model.DistributorProfileHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DistributorProfileHistoryRepository extends JpaRepository<DistributorProfileHistory, UUID>,
        JpaSpecificationExecutor<DistributorProfileHistory> {

}
