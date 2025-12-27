package com.wangkang.goodwillghservice.feature.k3cloud.service;

public interface K3cloudOrderService {

    /**
     * 从金蝶云同步修改的订单
     * 该方法会同步 最后修改时间 >= 当下时间 T - overlap 的订单
     * @param overlap 间隔，单位 s
     * @return 同步订单行数，一行和金蝶云的订单行一致，一个订单可以有多行分录组成
     */
    int syncModifiedOrder(long overlap);
}
