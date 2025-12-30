package com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.repository;

import com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.model.DistributorExternalInfo;
import com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.model.DistributorProfile;
import com.wangkang.goodwillghservice.dao.goodwillghservice.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DistributorProfileRepository extends JpaRepository<DistributorProfile, UUID>,
        JpaSpecificationExecutor<DistributorProfile> {

    boolean existsByUser(User user);

    boolean existsByExternalDistributor(DistributorExternalInfo externalInfo);

    DistributorProfile findByUser(User user);

    @Query("""
            select dp
            from DistributorProfile dp
            join fetch dp.externalDistributor
            """)
    List<DistributorProfile> findAllWithExternalDistributor();

    @Query("""
            select dp
            from DistributorProfile dp
            join fetch dp.externalDistributor
            WHERE dp.user.id = :distributorId
            """)
    DistributorProfile findByUserIdWithExternalDistributor(@Param("distributorId") UUID distributorId);

}
