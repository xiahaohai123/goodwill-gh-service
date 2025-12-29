package com.wangkang.goodwillghservice.dao.goodwillghservice.tilersale.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
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
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

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
}
