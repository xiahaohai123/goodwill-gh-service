package com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.repository;

import com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.model.DistributorExternalInfo;
import com.wangkang.goodwillghservice.feature.user.distributor.DistributorExternalInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DistributorExternalInfoRepository extends JpaRepository<DistributorExternalInfo, UUID>,
        JpaSpecificationExecutor<DistributorExternalInfo> {

    DistributorExternalInfo findByExternalId(Integer externalId);

    Page<DistributorExternalInfo> findAllByExternalCodeNotContainingIgnoreCase(String externalCode, Pageable pageable);

    @Query("""
        SELECT new com.wangkang.goodwillghservice.feature.user.distributor.DistributorExternalInfoDTO(
            e.id,
            e.externalName,
            e.externalCode,
            e.syncedAt,
            u.displayName
        )
        FROM DistributorExternalInfo e
        LEFT JOIN DistributorProfile p
            ON p.externalDistributor.id = e.id
           AND p.status = 'ACTIVE'
        LEFT JOIN User u
            ON p.user.id = u.id
        WHERE e.externalCode NOT LIKE %:excluded%
        """)
    Page<DistributorExternalInfoDTO> findExternalListExcluded(
            @Param("excluded") String excluded,
            Pageable pageable
    );

    @Query("""
    SELECT new com.wangkang.goodwillghservice.feature.user.distributor.DistributorExternalInfoDTO(
        e.id,
        e.externalName,
        e.externalCode,
        e.syncedAt,
        null
    )
    FROM DistributorExternalInfo e
    LEFT JOIN DistributorProfile p
        ON p.externalDistributor.id = e.id
       AND p.status = 'ACTIVE'
    WHERE p.id IS NULL
      AND e.externalCode NOT LIKE %:excluded%
    """)
    Page<DistributorExternalInfoDTO> findUnboundExternalListExcluded(
            @Param("excluded") String excluded,
            Pageable pageable
    );
}
