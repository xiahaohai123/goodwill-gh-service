package com.wangkang.goodwillghservice.dao.goodwillghservice.tilersale.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.UUIDJdbcType;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "sale_available_snapshot")
public class SaleAvailableSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcType(value = UUIDJdbcType.class)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    /** 经销商 */
    @Column(name = "distributor_id", nullable = false)
    private UUID distributorId;

    @Column(name = "color", nullable = false)
    private String color;
    @Column(name = "available", nullable = false)
    private Integer available;
    @Column(name = "based_on", nullable = false)
    private OffsetDateTime basedOn;
    /** 创建时间，强制要求手动填入，以确保批量保存的创建时间是相同的 */
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "batch_id", nullable = false)
    private UUID batchId;
    /** 本次快照同步到的贴砖工销售订单序列号，哨兵值: 0 */
    @Column(name = "tiler_sales_record_seq", nullable = false, updatable = false)
    private Long tilerSalesRecordSeq;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(UUID distributorId) {
        this.distributorId = distributorId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public OffsetDateTime getBasedOn() {
        return basedOn;
    }

    public void setBasedOn(OffsetDateTime basedOn) {
        this.basedOn = basedOn;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getBatchId() {
        return batchId;
    }

    public void setBatchId(UUID batchId) {
        this.batchId = batchId;
    }

    public Long getTilerSalesRecordSeq() {
        return tilerSalesRecordSeq;
    }

    public void setTilerSalesRecordSeq(Long tilerSalesRecordSeq) {
        this.tilerSalesRecordSeq = tilerSalesRecordSeq;
    }
}
