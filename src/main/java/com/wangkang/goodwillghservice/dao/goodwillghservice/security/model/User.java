package com.wangkang.goodwillghservice.dao.goodwillghservice.security.model;

import com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.model.DistributorProfile;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.UUIDJdbcType;

import java.time.OffsetDateTime;
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

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private DistributorProfile distributorProfile;

    @Column(name = "build_in", nullable = false)
    private Boolean buildIn = false;

    @Column(name = "inviter_id")
    private UUID inviterId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "banned_until")
    private OffsetDateTime bannedUntil;
    @Column(name = "banned_reason")
    private String bannedReason;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;
    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;
    @Column(name = "deleted_reason")
    private String deletedReason;

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

    public UUID getInviterId() {
        return inviterId;
    }

    public void setInviterId(UUID inviterId) {
        this.inviterId = inviterId;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getBannedUntil() {
        return bannedUntil;
    }

    public void setBannedUntil(OffsetDateTime bannedUntil) {
        this.bannedUntil = bannedUntil;
    }

    public String getBannedReason() {
        return bannedReason;
    }

    public void setBannedReason(String bannedReason) {
        this.bannedReason = bannedReason;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public OffsetDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(OffsetDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getDeletedReason() {
        return deletedReason;
    }

    public void setDeletedReason(String deletedReason) {
        this.deletedReason = deletedReason;
    }

    public DistributorProfile getDistributorProfile() {
        return distributorProfile;
    }

    public void setDistributorProfile(DistributorProfile distributorProfile) {
        this.distributorProfile = distributorProfile;
    }
}
