package com.wangkang.goodwillghservice.dao.goodwillghservice.order.repository;

import com.wangkang.goodwillghservice.dao.goodwillghservice.order.model.K3SaleOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface K3SaleOrderRepository extends JpaRepository<K3SaleOrder, UUID>,
        JpaSpecificationExecutor<K3SaleOrder> {

}
