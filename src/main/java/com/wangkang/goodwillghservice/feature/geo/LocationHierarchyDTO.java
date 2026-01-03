package com.wangkang.goodwillghservice.feature.geo;

import java.util.ArrayList;
import java.util.List;

public class LocationHierarchyDTO {
    private Long id;
    private String name;
    private Integer level;
    private List<LocationHierarchyDTO> children = new ArrayList<>();

    public LocationHierarchyDTO() {
    }

    // 方便转换的构造函数
    public LocationHierarchyDTO(Long id, String name, Integer level) {
        this.id = id;
        this.name = name;
        this.level = level;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<LocationHierarchyDTO> getChildren() {
        return children;
    }

    public void setChildren(List<LocationHierarchyDTO> children) {
        this.children = children;
    }
}
