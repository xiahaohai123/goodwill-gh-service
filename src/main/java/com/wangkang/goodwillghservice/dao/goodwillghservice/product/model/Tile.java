package com.wangkang.goodwillghservice.dao.goodwillghservice.product.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.UUIDJdbcType;

import java.util.UUID;

@Entity
@Table(name = "tile")
public class Tile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcType(value = UUIDJdbcType.class)
    private UUID id;
    @Column(nullable = false, unique = true)
    private String code;
    private String name;
    private String model;
    private String stockUnit;
    private Float weightGross;
    private String color;


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

    public Float getWeightGross() {
        return weightGross;
    }

    public void setWeightGross(Float weightGross) {
        this.weightGross = weightGross;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Material{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", model='" + model + '\'' +
                ", stockUnit='" + stockUnit + '\'' +
                ", weightGross=" + weightGross +
                ", color='" + color + '\'' +
                '}';
    }
}
