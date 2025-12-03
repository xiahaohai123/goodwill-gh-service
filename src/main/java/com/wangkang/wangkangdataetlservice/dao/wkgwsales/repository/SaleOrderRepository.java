package com.wangkang.wangkangdataetlservice.dao.wkgwsales.repository;

import com.wangkang.wangkangdataetlservice.dao.wkgwsales.model.SaleOrderPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@Repository
public interface SaleOrderRepository extends JpaRepository<SaleOrderPO, String> {


    Integer deleteByCreateDateGreaterThanEqualAndCreateDateLessThan(OffsetDateTime createDateIsGreaterThan,
                                                                    OffsetDateTime createDateIsLessThan);
}
