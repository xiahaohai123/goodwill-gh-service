package com.wangkang.wangkangdataetlservice.k3cloud.service;


import com.wangkang.wangkangdataetlservice.k3cloud.model.FormType;

import java.util.List;
import java.util.Map;

public interface K3cloudRequestService {
    /**
     * 对订单进行单据查询，查询出所有订单信息
     * @param filterString 过滤字符串
     * @param fieldKeys 要返回的信息
     * @return 订单列表
     */
    List<Map<String, Object>> billQueryOrderFieldsByFilter(String filterString, String fieldKeys);

    List<Map<String, Object>> billQueryOrderFieldsByFilter(String filterString,
                                                           String fieldKeys,
                                                           Integer startRow,
                                                           Integer limit);


    /**
     * 对销售出库单进行单据查询，查询出所有销售出库单信息
     * @param filterString 筛选信息
     * @param fieldKeys 需要返回的字段
     * @return 销售出库订单列表
     */
    List<Map<String, Object>> billQuerySaleOutFieldsByFilter(String filterString, String fieldKeys);
    /**
     * 对销售出库单进行单据查询，分页查询
     * @param filterString 筛选信息
     * @param fieldKeys 需要返回的字段
     * @param startRow 起始行
     * @param limit 结束行
     * @return 销售出库订单列表
     */
    List<Map<String, Object>> billQuerySaleOutFieldsByFilter(String filterString,
                                                           String fieldKeys,
                                                           Integer startRow,
                                                           Integer limit);

    /**
     * 对物料进行单据查询，查询出所有物料信息
     * @param filterString 查询字符串
     * @param fieldKeys 要查出来的字段
     * @return 物料信息列表
     */
    List<Map<String, Object>> billQueryMaterialFieldsByFilter(String filterString,
                                                              String fieldKeys);

    /**
     * 对物料单据进行查询，查询有限的数据集合
     * @param filterString 查询字符串
     * @param fieldKeys 要查出来的字段
     * @param startRow 起始行
     * @param limit 返回集合数量限制，不能超过 2000
     * @return 物料信息列表
     */
    List<Map<String, Object>> billQueryMaterialFieldsByFilter(String filterString,
                                                              String fieldKeys,   Integer startRow,
                                                              Integer limit);


    /**
     * 对任意可以使用 FUseOrgId （使用组织） 进行过滤的单据进行单据查询
     * 会增强过滤字符串，过滤使用组织为当前组织
     * @param formType 单据类型
     * @param filterString 过滤字符串
     * @param fieldKeys 要查询的字段
     * @return 返回所有行
     */
    List<Map<String, Object>> billQueryFieldsByFilterWithUserOrgId(FormType formType,
                                                                   String filterString,
                                                                   String fieldKeys);

    /**
     * 对任意可以使用 FStockOrgId （入库组织） 进行过滤的单据进行单据查询
     * 会增强过滤字符串，过滤使用组织为当前组织
     * @param formType 单据类型
     * @param filterString 过滤字符串
     * @param fieldKeys 要查询的字段
     * @return 返回所有行
     */
    List<Map<String, Object>> billQueryFieldsByFilterWithStockOrgId(FormType formType,
                                                                    String filterString,
                                                                    String fieldKeys);

    /**
     * 对任意单据进行单据查询
     * @param formType 单据类型
     * @param filterString 查询字符串
     * @param fieldKeys 要查询的字段
     * @return 返回所有行
     */
    List<Map<String, Object>> billQueryFieldsByFilter(FormType formType,
                                                      String filterString,
                                                      String fieldKeys);

    List<Map<String, Object>> billQueryFieldsByFilter(FormType formType,
                                                      String filterString,
                                                      String fieldKeys,
                                                      Integer startRow,
                                                      Integer limit);

    String view(String formId,String queryString);

}
