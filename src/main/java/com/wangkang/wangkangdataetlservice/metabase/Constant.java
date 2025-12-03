package com.wangkang.wangkangdataetlservice.metabase;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Constant {


    public static final Map<String, String> DISCOUNT_NAME_2_CODE;
    public static final Map<String, String> DISCOUNT_CODE_2_NAME;
    public static final Map<String, String> ORDER_BI_XLSX_KEY_2_HEADER;
    public static final Map<String, String> SALE_OUT_BI_XLSX_KEY_2_HEADER;

    static {
        Map<String, String> tempDiscountName2Code = new HashMap<>();
        tempDiscountName2Code.put("现金折扣", "D");
        tempDiscountName2Code.put("返货", "A");
        tempDiscountName2Code.put("赠品", "C");
        tempDiscountName2Code.put("礼品", "B");
        tempDiscountName2Code.put("广告宣传", "E");
        tempDiscountName2Code.put("运费补贴", "G");
        tempDiscountName2Code.put("销售样品", "H");
        tempDiscountName2Code.put("价格补贴", "F");
        tempDiscountName2Code.put("货损赔偿", "I");
        tempDiscountName2Code.put("处理尾货", "J");
        tempDiscountName2Code.put("付款折扣", "K");
        tempDiscountName2Code.put(null, null);
        DISCOUNT_NAME_2_CODE = Collections.unmodifiableMap(tempDiscountName2Code);

        Map<String, String> tempDiscountCode2Name = new HashMap<>();
        for (Map.Entry<String, String> entry : DISCOUNT_NAME_2_CODE.entrySet()) {
            tempDiscountCode2Name.put(entry.getValue(), entry.getKey());
        }
        tempDiscountCode2Name.put("", "无折扣");
        tempDiscountCode2Name.put(null, "无折扣");
        DISCOUNT_CODE_2_NAME = Collections.unmodifiableMap(tempDiscountCode2Name);

        ORDER_BI_XLSX_KEY_2_HEADER = Map.ofEntries(
                Map.entry("billNo", "销售订单号"),
                Map.entry("date", "开单日期"),
                Map.entry("billType", "单据类型"),
                Map.entry("documentStatus", "单据状态"),
                Map.entry("customerNumber", "客户编码"),
                Map.entry("customerName", "客户名称"),
                Map.entry("saleDepartment", "销售部门"),
                Map.entry("closeStatus", "关闭状态"),
                Map.entry("city", "销售城市"),
                Map.entry("settlementCurrency", "结算币别"),
                Map.entry("customerCountry", "客户国家"),
                Map.entry("province", "销售省份"),
                Map.entry("materialNumber", "物料编码"),
                Map.entry("materialName", "物料名称"),
                Map.entry("unit", "销售单位"),
                Map.entry("quantity", "销售数量"),
                Map.entry("specificationModel", "规格型号"),
                Map.entry("price", "单价"),
                Map.entry("amount", "销售金额"),
                Map.entry("discountType", "折扣类型")
        );

        SALE_OUT_BI_XLSX_KEY_2_HEADER = Map.ofEntries(
                Map.entry("billNo", "出库单号"),
                Map.entry("date", "下推出库日期"),
                Map.entry("documentStatus", "单据状态"),
                Map.entry("saleOrderNumber", "销售订单号"),
                Map.entry("customerName", "客户名称"),
                Map.entry("customerAddress", "客户地址"),
                Map.entry("quantity", "实发数量"),
                Map.entry("materialNumber", "物料编码"),
                Map.entry("materialName", "物料名称"),
                Map.entry("specificationModel", "规格型号"),
                Map.entry("unit", "库存单位"),
                Map.entry("price", "单价"),
                Map.entry("amount", "销售金额"),
                Map.entry("licensePlate", "车牌号"),
                Map.entry("discountType", "折扣类型"),
                Map.entry("settlementCurrency", "结算币别"),
                Map.entry("city", "销售城市"),
                Map.entry("country", "客户国家"),
                Map.entry("stockLocationName", "仓位"),
                Map.entry("province", "销售省份"),
                Map.entry("carrierName", "承运商")
        );
    }


    private Constant() {
    }
}
