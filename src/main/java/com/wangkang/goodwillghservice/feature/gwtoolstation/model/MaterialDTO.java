package com.wangkang.goodwillghservice.feature.gwtoolstation.model;

import java.math.BigDecimal;
import java.util.UUID;

public class MaterialDTO {
    private UUID id;
    private String code;
    private String name;
    private String model;
    private String stockUnit;
    private BigDecimal weightGross;
    private String color;

    public BigDecimal getWeightGross() {
        return weightGross;
    }

    public void setWeightGross(BigDecimal weightGross) {
        this.weightGross = weightGross;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getStockUnit() {
        return stockUnit;
    }

    public void setStockUnit(String stockUnit) {
        this.stockUnit = stockUnit;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}