package com.wangkang.wangkangdataetlservice.metabase.sale;

import com.wangkang.wangkangdataetlservice.k3cloud.model.OrderBillType;
import com.wangkang.wangkangdataetlservice.k3cloud.model.OrderDocumentStatus;
import com.wangkang.wangkangdataetlservice.k3cloud.model.OrderVerifyStatus;
import org.apache.commons.lang3.StringUtils;

import static com.wangkang.wangkangdataetlservice.k3cloud.model.OrderBillType.ORDER_TYPE_CODE_DESC;
import static com.wangkang.wangkangdataetlservice.k3cloud.model.OrderCloseStatus.CLOSE_STATUS_CODE_2_NAME;
import static com.wangkang.wangkangdataetlservice.k3cloud.model.OrderDocumentStatus.DOCUMENT_STATUS_CODE_2_DESC;
import static com.wangkang.wangkangdataetlservice.k3cloud.model.OrderVerifyStatus.VERIFY_CODE_DESC;
import static com.wangkang.wangkangdataetlservice.metabase.Constant.DISCOUNT_CODE_2_NAME;


public class SaleOrder {
    /** 订单编号 */
    private String billNo;
    /**
     * 创建日期，好像是根据登录者的时区
     * @see SaleOrder#createDate
     */
    private String date;
    /** 创建日期 原始数据 UTC+8 区 */
    private String createDate;
    /**
     * 单据类型
     * @see OrderBillType
     */
    private String billType;
    /**
     * 单据状态
     * @see OrderDocumentStatus
     */
    private String documentStatus;
    /** 客户编码 */
    private String customerNumber;
    /** 客户名称 */
    private String customerName;
    /** 销售部门 */
    private String saleDepartment;

    /** 客户国家 */
    private String customerCountry;
    /** 省份 */
    private String province;
    /** 城市 */
    private String city;

    /** 物料编码 */
    private String materialNumber;
    /** 物料名称 */
    private String materialName;
    /** 规格型号 */
    private String specificationModel;
    /** 销售单位 */
    private String unit;
    /** 数量 */
    private Integer quantity;
    /** 折扣类型 */
    private String discountType;
    /** 结算币种 */
    private String settlementCurrency;
    /** 价格 */
    private Double price;
    /** 总价 */
    private Double amount;
    /**
     * 复核状态 (财务签单)
     * @see OrderVerifyStatus
     */
    private String verifyStatus;
    /** 复核日期 */
    private String verifyDate;
    /** 关闭状态 */
    private String closeStatus;
    /** 关闭日期 */
    private String closeDate;
    private Double totalWeightTon;


    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        String cur = billType;
        if (ORDER_TYPE_CODE_DESC.containsKey(billType)) {
            cur = ORDER_TYPE_CODE_DESC.get(billType);
        }
        this.billType = cur;
    }

    public String getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(String documentStatus) {
        String cur = documentStatus;
        if (DOCUMENT_STATUS_CODE_2_DESC.containsKey(documentStatus)) {
            cur = DOCUMENT_STATUS_CODE_2_DESC.get(documentStatus);
        }
        this.documentStatus = cur;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSaleDepartment() {
        return saleDepartment;
    }

    public void setSaleDepartment(String saleDepartment) {
        this.saleDepartment = saleDepartment;
    }

    public String getCloseStatus() {
        return closeStatus;
    }

    public void setCloseStatus(String closeStatus) {
        String cur = closeStatus;
        if (CLOSE_STATUS_CODE_2_NAME.containsKey(closeStatus)) {
            cur = CLOSE_STATUS_CODE_2_NAME.get(closeStatus);
        }
        this.closeStatus = cur;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSettlementCurrency() {
        return settlementCurrency;
    }

    public void setSettlementCurrency(String settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public String getCustomerCountry() {
        return customerCountry;
    }

    public void setCustomerCountry(String customerCountry) {
        this.customerCountry = customerCountry;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        if (StringUtils.isBlank(province)) {
            this.province = null;
        } else {
            this.province = province;
        }
    }

    public String getMaterialNumber() {
        return materialNumber;
    }

    public void setMaterialNumber(String materialNumber) {
        this.materialNumber = materialNumber;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getSpecificationModel() {
        return specificationModel;
    }

    public void setSpecificationModel(String specificationModel) {
        this.specificationModel = specificationModel;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        String cur = discountType;
        if (cur != null) {
            cur = cur.trim();
        }
        if (DISCOUNT_CODE_2_NAME.containsKey(cur)) {
            cur = DISCOUNT_CODE_2_NAME.get(cur);
        }
        this.discountType = cur;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setVerifyStatus(String verifyStatus) {
        String cur = verifyStatus;
        if (VERIFY_CODE_DESC.containsKey(verifyStatus)) {
            cur = VERIFY_CODE_DESC.get(verifyStatus);
        }
        this.verifyStatus = cur;
    }

    public String getVerifyStatus() {
        return verifyStatus;
    }

    public String getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(String verifyDate) {
        this.verifyDate = verifyDate;
    }

    public String getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(String closeDate) {
        this.closeDate = closeDate;
    }

    public Double getTotalWeightTon() {
        return totalWeightTon;
    }

    public void setTotalWeightTon(Double totalWeightTon) {
        this.totalWeightTon = totalWeightTon;
    }
}
