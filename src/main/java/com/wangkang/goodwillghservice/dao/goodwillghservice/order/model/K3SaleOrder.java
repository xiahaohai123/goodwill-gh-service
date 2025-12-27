package com.wangkang.goodwillghservice.dao.goodwillghservice.order.model;

import com.wangkang.goodwillghservice.feature.k3cloud.model.OrderCloseStatus;
import com.wangkang.goodwillghservice.feature.k3cloud.model.OrderDocumentStatus;
import com.wangkang.goodwillghservice.feature.k3cloud.model.OrderVerifyStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.descriptor.jdbc.UUIDJdbcType;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "k3_sale_order")
public class K3SaleOrder {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcType(value = UUIDJdbcType.class)
    private UUID id;
    @Column(name = "bill_no", nullable = false)
    private String billNo;
    @Column(name = "customer_id", nullable = false)
    private Integer customerId;
    @Column(name = "customer_number")
    private String customerNumber;
    @Column(name = "customer_name")
    private String customerName;
    @Column(name = "create_date", nullable = false)
    private OffsetDateTime createdDate;
    @Column(name = "document_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderDocumentStatus documentStatus;
    @Column(name = "material_number", nullable = false)
    private String materialNumber;
    @Column(name = "unit", nullable = false)
    private String unit;
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    @Enumerated(EnumType.STRING)
    @Column(name = "verify_status", nullable = false)
    private OrderVerifyStatus verifyStatus;
    @Column(name = "verify_date")
    private OffsetDateTime verifyDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "close_status", nullable = false)
    private OrderCloseStatus closeStatus;
    @Column(name = "close_date")
    private OffsetDateTime closeDate;
    @Column(name = "last_modify_date", nullable = false)
    private OffsetDateTime lastModifyDate;
    @UpdateTimestamp
    @Column(name = "synced_at", nullable = false)
    private OffsetDateTime syncedAt;

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

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
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

    public OffsetDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(OffsetDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public OrderDocumentStatus getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(OrderDocumentStatus documentStatus) {
        this.documentStatus = documentStatus;
    }

    public String getMaterialNumber() {
        return materialNumber;
    }

    public void setMaterialNumber(String materialNumber) {
        this.materialNumber = materialNumber;
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

    public OrderVerifyStatus getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(OrderVerifyStatus verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public OffsetDateTime getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(OffsetDateTime verifyDate) {
        this.verifyDate = verifyDate;
    }

    public OrderCloseStatus getCloseStatus() {
        return closeStatus;
    }

    public void setCloseStatus(OrderCloseStatus closeStatus) {
        this.closeStatus = closeStatus;
    }

    public OffsetDateTime getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(OffsetDateTime closeDate) {
        this.closeDate = closeDate;
    }

    public OffsetDateTime getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(OffsetDateTime lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public OffsetDateTime getSyncedAt() {
        return syncedAt;
    }

    public void setSyncedAt(OffsetDateTime syncedAt) {
        this.syncedAt = syncedAt;
    }
}
