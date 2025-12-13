package com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.model;


import jakarta.persistence.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.UUIDJdbcType;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "distributor_external_info")
public class DistributorExternalInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcType(value = UUIDJdbcType.class)
    private UUID id;
    @Column(name = "external_id", nullable = false)
    private Integer externalId;
    @Column(name = "external_name", nullable = false)
    private String externalName;
    @Column(name = "external_code")
    private String externalCode;
    @Column(name = "synced_at", nullable = false)
    private OffsetDateTime syncedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getExternalId() {
        return externalId;
    }

    public void setExternalId(Integer externalId) {
        this.externalId = externalId;
    }

    public String getExternalName() {
        return externalName;
    }

    public void setExternalName(String externalName) {
        this.externalName = externalName;
    }

    public String getExternalCode() {
        return externalCode;
    }

    public void setExternalCode(String externalCode) {
        this.externalCode = externalCode;
    }

    public OffsetDateTime getSyncedAt() {
        return syncedAt;
    }

    public void setSyncedAt(OffsetDateTime syncedAt) {
        this.syncedAt = syncedAt;
    }
}
