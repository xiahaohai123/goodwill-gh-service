package com.wangkang.wangkangdataetlservice.dao.wkgwsales.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.UUIDJdbcType;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "inventory")
public class InventoryPO {
    private static final Map<String, String> dataStatusMapping = Map.of("C", "已审核");
    private static final Map<String, String> forbidStatusMapping = Map.of("A", "否");
    private static final Map<String, String> materialPropertiesMapping = Map.of("1", "外购", "2", "自制");

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcType(value = UUIDJdbcType.class)
    private UUID id;
    /** 使用组织 */
    private String organization;
    /** 编码 */
    @Column(nullable = false, unique = true)
    private String skuCode;
    /** 名称 */
    private String name;
    /** 规格型号 */
    private String model;
    /** 数据状态 */
    private String dataStatus;
    /** 禁用状态 */
    private String banStatus;
    /** 物料属性 */
    private String materialProperties;
    /** 库存单位 */
    private String inventoryUnit;
    /** 可用量 */
    private Float inventoryAvailable;
    /** 库存量 */
    private Float inventory;
    private LocalDateTime createAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
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

    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        String cur = dataStatus;
        if (dataStatusMapping.containsKey(dataStatus)) {
            cur = dataStatusMapping.get(dataStatus);
        }
        this.dataStatus = cur;
    }

    public String getBanStatus() {
        return banStatus;
    }

    public void setBanStatus(String banStatus) {
        String cur = banStatus;
        if (forbidStatusMapping.containsKey(banStatus)) {
            cur = forbidStatusMapping.get(banStatus);
        }
        this.banStatus = cur;
    }

    public String getMaterialProperties() {
        return materialProperties;
    }

    public void setMaterialProperties(String materialProperties) {
        String cur = materialProperties;
        if (materialPropertiesMapping.containsKey(materialProperties)) {
            cur = materialPropertiesMapping.get(materialProperties);
        }
        this.materialProperties = cur;
    }

    public String getInventoryUnit() {
        return inventoryUnit;
    }

    public void setInventoryUnit(String inventoryUnit) {
        this.inventoryUnit = inventoryUnit;
    }

    public Float getInventoryAvailable() {
        return inventoryAvailable;
    }

    public void setInventoryAvailable(Float inventoryAvailable) {
        this.inventoryAvailable = inventoryAvailable;
    }

    public Float getInventory() {
        return inventory;
    }

    public void setInventory(Float inventory) {
        this.inventory = inventory;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public void updateData(InventoryPO inventoryPO) {
        setOrganization(inventoryPO.getOrganization());
        setInventory(inventoryPO.getInventory());
        setInventoryAvailable(inventoryPO.getInventoryAvailable());
        setInventoryUnit(inventoryPO.getInventoryUnit());
        setName(inventoryPO.getName());
        setModel(inventoryPO.getModel());
        setMaterialProperties(inventoryPO.getMaterialProperties());
        setDataStatus(inventoryPO.getDataStatus());
        setBanStatus(inventoryPO.getBanStatus());
        setCreateAt(inventoryPO.getCreateAt());
    }
}
