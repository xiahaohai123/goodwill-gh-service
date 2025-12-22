package com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.model;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.UUIDJdbcType;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "distributor_profile_history")
public class DistributorProfileHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcType(UUIDJdbcType.class)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "distributor_profile_id")
    private UUID distributorProfileId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "external_distributor_id", nullable = false)
    private UUID externalDistributorId;

    @Column(name = "action", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status action; // BIND / SUSPEND

    @Column(name = "operated_by")
    private UUID operatedBy;

    @CreationTimestamp
    @Column(name = "operated_at", nullable = false)
    private OffsetDateTime operatedAt;

    @Column(name = "reason")
    private String reason;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDistributorProfileId() {
        return distributorProfileId;
    }

    public void setDistributorProfileId(UUID distributorProfileId) {
        this.distributorProfileId = distributorProfileId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getExternalDistributorId() {
        return externalDistributorId;
    }

    public void setExternalDistributorId(UUID externalDistributorId) {
        this.externalDistributorId = externalDistributorId;
    }

    public Status getAction() {
        return action;
    }

    public void setAction(Status action) {
        this.action = action;
    }

    public UUID getOperatedBy() {
        return operatedBy;
    }

    public void setOperatedBy(UUID operatedBy) {
        this.operatedBy = operatedBy;
    }

    public OffsetDateTime getOperatedAt() {
        return operatedAt;
    }

    public void setOperatedAt(OffsetDateTime operatedAt) {
        this.operatedAt = operatedAt;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
