package com.wangkang.goodwillghservice.feature.k3cloud.service;

import com.wangkang.goodwillghservice.feature.k3cloud.model.customer.Customer;

import java.util.List;

public interface K3cloudCustomerService {
    /**
     * 获取客户信息列表
     * @return 客户信息列表
     */
    List<Customer> getCustomerList();
}
