package com.wangkang.goodwillghservice.dao.goodwillghservice.security.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.UUIDJdbcType;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tbl_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcType(value = UUIDJdbcType.class)
    private UUID id;
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    @Column(name = "area_code", nullable = false)
    private String areaCode;
    @Column(name = "display_name", nullable = false)
    private String displayName;
    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_permission_group",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Set<PermissionGroup> groups;

    @Column(name = "build_in", nullable = false)
    private Boolean buildIn = false;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<PermissionGroup> getGroups() {
        return groups;
    }

    public void setGroups(Set<PermissionGroup> groups) {
        this.groups = groups;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Boolean getBuildIn() {
        return buildIn;
    }

    public void setBuildIn(Boolean buildIn) {
        this.buildIn = buildIn;
    }
}
