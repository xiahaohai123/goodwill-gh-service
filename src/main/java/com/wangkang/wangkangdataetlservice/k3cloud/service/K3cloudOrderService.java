package com.wangkang.wangkangdataetlservice.k3cloud.service;


import com.wangkang.wangkangdataetlservice.metabase.sale.SaleOrder;

import java.util.Collection;

public interface K3cloudOrderService {

    Collection<SaleOrder> getOrderBIListByCreateDate(String startDate, String endDate);

    Collection<SaleOrder> getOrderBIListByCreateDate(String startDate, String endDate, int startIndex, int limit);
}
