package com.wangkang.wangkangdataetlservice.k3cloud.model;

public enum FormType {
    SALE_ORDER("SAL_SaleOrder"),
    SALE_OUT_STOCK("SAL_OUTSTOCK"),
    SALE_QUOTATION("SAL_QUOTATION"),
    BD_MATERIAL("BD_MATERIAL"),
    FREIGHT_LIST("ora_SalTranList"),
    CUSTOMER_ADDRESS("ora_CustAddress"),
    BD_CUSTOMER("BD_Customer"),
    BD_SUPPLIER("BD_Supplier"),
    SP_IN_STOCK("SP_InStock"),
    ;


    private final String id;

    FormType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
