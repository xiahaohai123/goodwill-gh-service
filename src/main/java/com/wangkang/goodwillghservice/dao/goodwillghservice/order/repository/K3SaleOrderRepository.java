package com.wangkang.goodwillghservice.dao.goodwillghservice.order.repository;

import com.wangkang.goodwillghservice.dao.goodwillghservice.order.model.K3SaleOrder;
import com.wangkang.goodwillghservice.feature.k3cloud.model.OrderCloseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface K3SaleOrderRepository extends JpaRepository<K3SaleOrder, UUID>,
        JpaSpecificationExecutor<K3SaleOrder> {

    int deleteByBillNoIn(Collection<String> billNos);

    @Query("SELECT DISTINCT k.billNo FROM K3SaleOrder k WHERE k.closeStatus = :closeStatus")
    List<String> findDistinctBillNoByCloseStatus(@Param("closeStatus") OrderCloseStatus closeStatus);
}
