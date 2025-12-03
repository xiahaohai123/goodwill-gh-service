package com.wangkang.wangkangdataetlservice.dao.wkgwsales.model;

import com.wangkang.wangkangdataetlservice.k3cloud.model.OrderBillType;
import com.wangkang.wangkangdataetlservice.k3cloud.model.OrderDocumentStatus;
import com.wangkang.wangkangdataetlservice.k3cloud.model.OrderVerifyStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.UUIDJdbcType;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "sale_order")
public class SaleOrderPO {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcType(value = UUIDJdbcType.class)
    private UUID id;
    /** 订单编号 */
    private String billNo;
    /**
     * 创建日期，好像是根据登录者的时区
     * @see SaleOrderPO#createDate
     */
    private OffsetDateTime date;
    /** 创建日期 原始数据 UTC+8 区 */
    private OffsetDateTime createDate;
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
    private OffsetDateTime verifyDate;
    /** 关闭状态 */
    private String closeStatus;
    /** 关闭日期 */
    private OffsetDateTime closeDate;
    @Column(name = "total_weight_ton")
    private Double totalWeightTon;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(OffsetDateTime createDate) {
        this.createDate = createDate;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(String documentStatus) {
        this.documentStatus = documentStatus;
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
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getSpecificationModel() {
        return specificationModel;
    }

    public void setSpecificationModel(String specificationModel) {
        this.specificationModel = specificationModel;
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

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getSettlementCurrency() {
        return settlementCurrency;
    }

    public void setSettlementCurrency(String settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
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

    public String getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(String verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public OffsetDateTime getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(OffsetDateTime verifyDate) {
        this.verifyDate = verifyDate;
    }

    public String getCloseStatus() {
        return closeStatus;
    }

    public void setCloseStatus(String closeStatus) {
        this.closeStatus = closeStatus;
    }

    public OffsetDateTime getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(OffsetDateTime closeDate) {
        this.closeDate = closeDate;
    }

    public Double getTotalWeightTon() {
        return totalWeightTon;
    }

    public void setTotalWeightTon(Double totalWeightTon) {
        this.totalWeightTon = totalWeightTon;
    }
}
