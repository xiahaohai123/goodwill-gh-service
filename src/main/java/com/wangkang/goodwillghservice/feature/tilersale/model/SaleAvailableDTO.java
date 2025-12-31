package com.wangkang.goodwillghservice.feature.tilersale.model;

import java.util.UUID;

/**
 * 可用销售量信息
 */
public class SaleAvailableDTO {
    private UUID distributorId;
    private String color;
    private Integer available;

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
}
