package com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.repository;

import com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.model.DistributorExternalInfo;
import com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.model.DistributorProfile;
import com.wangkang.goodwillghservice.dao.goodwillghservice.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DistributorProfileRepository extends JpaRepository<DistributorProfile, UUID>,
        JpaSpecificationExecutor<DistributorProfile> {

    boolean existsByUser(User user);

    boolean existsByExternalDistributor(DistributorExternalInfo externalInfo);

    DistributorProfile findByUser(User user);
}
