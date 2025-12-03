package com.wangkang.wangkangdataetlservice.dao.wkgwsales.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.UUIDJdbcType;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "final_good_snapshot")
public class FinalGoodsSnapshotPO {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcType(value = UUIDJdbcType.class)
    private UUID id;

    /** 编码 */
    @Column(name = "material_code", nullable = false)
    private String materialCode;
    /** 名称 */
    @Column(nullable = false)
    private String name;
    /** 规格型号 */
    @Column(nullable = false)
    private String model;

    /** 库存单位 */
    @Column(name = "inventory_unit", nullable = false)
    private String inventoryUnit;
    /** 可用量 */
    @Column(name = "inventory_available", nullable = false)
    private Double inventoryAvailable;
    /** 库存量 */
    @Column(nullable = false)
    private Double inventory;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getInventoryUnit() {
        return inventoryUnit;
    }

    public void setInventoryUnit(String inventoryUnit) {
        this.inventoryUnit = inventoryUnit;
    }

    public Double getInventoryAvailable() {
        return inventoryAvailable;
    }

    public void setInventoryAvailable(Double inventoryAvailable) {
        this.inventoryAvailable = inventoryAvailable;
    }

    public Double getInventory() {
        return inventory;
    }

    public void setInventory(Double inventory) {
        this.inventory = inventory;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
