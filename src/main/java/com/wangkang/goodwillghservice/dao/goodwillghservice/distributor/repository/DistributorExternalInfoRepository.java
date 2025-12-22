package com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.repository;

import com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.model.DistributorExternalInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DistributorExternalInfoRepository extends JpaRepository<DistributorExternalInfo, UUID>,
        JpaSpecificationExecutor<DistributorExternalInfo> {

    DistributorExternalInfo findByExternalId(Integer externalId);

    Page<DistributorExternalInfo> findAllByExternalCodeNotContainingIgnoreCase(String externalCode, Pageable pageable);
}
