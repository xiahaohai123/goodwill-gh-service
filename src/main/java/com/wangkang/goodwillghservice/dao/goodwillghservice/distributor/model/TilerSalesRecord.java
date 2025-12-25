package com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.UUIDJdbcType;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "tiler_sales_record")
public class TilerSalesRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcType(value = UUIDJdbcType.class)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    // ========= 业务维度 =========

    /** 花色 / 产品编码 */
    @Column(name = "product_code")
    private String productCode;

    /** 经销商 / 门店 / 用户 */
    @Column(name = "distributor_id", nullable = false)
    private UUID distributorId;

    /** 瓦工 */
    @Column(name = "tiler_id", nullable = false)
    private UUID tilerId;

    // ========= 数量与金额 =========

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    // ========= 状态与来源 =========

    /** CREATED / CONFIRMED / CANCELLED / REFUNDED */
    @Column(name = "status", nullable = false)
    private String status;

    // ========= 时间维度 =========

    /** 实际成交时间 */
    @CreationTimestamp
    @Column(name = "sale_time", nullable = false)
    private OffsetDateTime saleTime;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public UUID getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(UUID distributorId) {
        this.distributorId = distributorId;
    }

    public UUID getTilerId() {
        return tilerId;
    }

    public void setTilerId(UUID tilerId) {
        this.tilerId = tilerId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OffsetDateTime getSaleTime() {
        return saleTime;
    }

    public void setSaleTime(OffsetDateTime saleTime) {
        this.saleTime = saleTime;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
