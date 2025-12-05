package com.wangkang.goodwillghservice.dao.goodwillghservice.audit.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.UUIDJdbcType;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "login_log")
public class LoginLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcType(value = UUIDJdbcType.class)
    private UUID id;

    @JdbcType(value = UUIDJdbcType.class)
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "username")
    private String username;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    @Column(name = "area_code", nullable = false)
    private String areaCode;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(nullable = false)
    private String status;

    @Column(name = "failure_reason")
    private String failureReason;

    @Column(name = "occur_at", nullable = false, updatable = false)
    private OffsetDateTime occurAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public OffsetDateTime getOccurAt() {
        return occurAt;
    }

    public void setOccurAt(OffsetDateTime occurAt) {
        this.occurAt = occurAt;
    }

    @PrePersist
    protected void onCreate() {
        this.occurAt = OffsetDateTime.now();
    }
}
