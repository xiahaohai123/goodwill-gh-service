package com.wangkang.wangkangdataetlservice.dao.dataetlservice.security.model;

import com.wangkang.wangkangdataetlservice.dao.dataetlservice.security.PermissionSetConverter;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.hibernate.type.descriptor.jdbc.UUIDJdbcType;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "permission_group")
public class PermissionGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcType(value = UUIDJdbcType.class)
    private UUID id;

    private String name; // 权限组名

    @Convert(converter = PermissionSetConverter.class)
    @Column(columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON) // ✅ 关键：明确告诉 Hibernate 这是 JSON 类型
    private Set<Permission> permissions; // 存储 Permission 集合的 JSON

    @Column(name = "build_in", nullable = false)
    private Boolean buildIn = false;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Boolean getBuildIn() {
        return buildIn;
    }

    public void setBuildIn(Boolean buildIn) {
        this.buildIn = buildIn;
    }
}
