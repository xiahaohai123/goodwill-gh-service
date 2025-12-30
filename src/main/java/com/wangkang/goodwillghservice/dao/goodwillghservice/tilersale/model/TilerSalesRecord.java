package com.wangkang.goodwillghservice.dao.goodwillghservice.tilersale.model;

import jakarta.persistence.*;
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
    @Column(name = "product_color")
    private String productColor;

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

    /** SALE 销售 / CANCEL 撤销 / ADJUSTMENT 调整 */
    @Enumerated(EnumType.STRING)
    @Column(name = "record_type", nullable = false)
    private TilerSalesRecordType recordType;

    // ========= 时间维度 =========

    /** 实际成交时间 */
    @Column(name = "sale_time", nullable = false)
    private OffsetDateTime saleTime;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    /**
     * DB 生成的严格递增序列，用于快照边界
     * 由 PostgreSQL sequence + DEFAULT nextval(...) 负责
     */
    @Column(
            name = "seq",
            nullable = false,
            updatable = false,
            insertable = false
    )
    private Long seq;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProductColor() {
        return productColor;
    }

    public void setProductColor(String productCode) {
        this.productColor = productCode;
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

    public TilerSalesRecordType getRecordType() {
        return recordType;
    }

    public void setRecordType(TilerSalesRecordType recordType) {
        this.recordType = recordType;
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

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }
}
