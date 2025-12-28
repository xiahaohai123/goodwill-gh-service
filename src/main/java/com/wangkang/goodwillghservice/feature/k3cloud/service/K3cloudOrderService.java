package com.wangkang.goodwillghservice.feature.k3cloud.service;

public interface K3cloudOrderService {

    /**
     * 从金蝶云同步修改的订单
     * 该方法会同步 最后修改时间 >= 当下时间 T - overlap 的订单
     * 本方法线程不安全，自行构建共享锁以确保线程安全
     * 本方法不记录审计日志，一般用于同步器调用
     * @see K3cloudOrderService#syncModifiedOrderAndAudit(long) 会记录审计日志的同类方法
     * @param overlap 间隔，单位 s
     * @return 同步订单行数，一行和金蝶云的订单行一致，一个订单可以有多行分录组成
     */
    int syncModifiedOrder(long overlap);

    /**
     * 从金蝶云同步修改的订单
     * 该方法会同步 最后修改时间 >= 当下时间 T - overlap 的订单
     * 本方法线程不安全，自行构建共享锁以确保线程安全
     * 本方法会记录审计日志，一般用于人工调用
     * @see K3cloudOrderService#syncModifiedOrder(long) 不记录审计日志的同类方法
     * @param overlap 间隔，单位 s
     * @return 同步订单行数，一行和金蝶云的订单行一致，一个订单可以有多行分录组成
     */
    int syncModifiedOrderAndAudit(long overlap);
}
