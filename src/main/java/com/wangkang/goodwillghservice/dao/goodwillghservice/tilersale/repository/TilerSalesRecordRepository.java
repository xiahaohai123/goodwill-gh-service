package com.wangkang.goodwillghservice.dao.goodwillghservice.tilersale.repository;

import com.wangkang.goodwillghservice.dao.goodwillghservice.tilersale.model.ColorDeltaProjection;
import com.wangkang.goodwillghservice.dao.goodwillghservice.tilersale.model.TilerSalesRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TilerSalesRecordRepository extends JpaRepository<TilerSalesRecord, UUID>,
        JpaSpecificationExecutor<TilerSalesRecord> {

    Page<TilerSalesRecord> findAllByDistributorIdAndProductColor(UUID distributorId,
                                                                 String productColor,
                                                                 Pageable pageable);

    @Query("""
                select r.productColor as color,
                       sum(
                           case
                               when r.recordType = com.wangkang.goodwillghservice.dao.goodwillghservice.tilersale.model.TilerSalesRecordType.SALE then -r.quantity
                               when r.recordType = com.wangkang.goodwillghservice.dao.goodwillghservice.tilersale.model.TilerSalesRecordType.CANCEL then r.quantity
                               else 0
                           end
                       ) as delta
                from TilerSalesRecord r
                where r.distributorId = :distributorId
                  and r.seq <= :upperSeq
                group by r.productColor
            """)
    List<ColorDeltaProjection> sumDeltaGroupByColorBeforeOrEqualSeq(
            @Param("distributorId") UUID distributorId,
            @Param("upperSeq") long upperSeq
    );

    @Query("""
                select r.productColor as color,
                       sum(
                           case
                               when r.recordType = com.wangkang.goodwillghservice.dao.goodwillghservice.tilersale.model.TilerSalesRecordType.SALE then -r.quantity
                               when r.recordType = com.wangkang.goodwillghservice.dao.goodwillghservice.tilersale.model.TilerSalesRecordType.CANCEL then r.quantity
                               else 0
                           end
                       ) as delta
                from TilerSalesRecord r
                where r.distributorId = :distributorId
                  and r.seq >= :fromSeq
                  and r.seq <= :upperSeq
                group by r.productColor
            """)
    List<ColorDeltaProjection> sumDeltaGroupByColorAfterOrEqualFromSeqBeforeOrEqualSeq(
            @Param("distributorId") UUID distributorId,
            @Param("fromSeq") long fromSeq,
            @Param("upperSeq") long upperSeq
    );

    @Query("select max(t.seq) from TilerSalesRecord t WHERE t.distributorId = :distributorId")
    Long findMaxSeq(@Param("distributorId") UUID distributorId);
}
