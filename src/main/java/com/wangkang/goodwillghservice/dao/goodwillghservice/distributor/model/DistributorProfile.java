package com.wangkang.goodwillghservice.dao.goodwillghservice.distributor.model;

import com.wangkang.goodwillghservice.dao.goodwillghservice.security.model.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.UUIDJdbcType;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "distributor_profile", uniqueConstraints = {@UniqueConstraint(columnNames = "user_id"), @UniqueConstraint(columnNames = "external_distributor_id")})
public class DistributorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcType(UUIDJdbcType.class)
    private UUID id;

    /**
     * 用户 ↔ 经销商 1:1
     * 由 distributor_profile 持有外键，是“关系所有方”
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    /**
     * 外部经销商信息
     * 语义是 ManyToOne，但数据库唯一约束保证不会真的多对一
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "external_distributor_id", nullable = false, updatable = false)
    private DistributorExternalInfo externalDistributor;

    @CreationTimestamp
    @Column(name = "bound_at", nullable = false, updatable = false)
    private OffsetDateTime boundAt;

    // ---------- getter / setter ----------

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DistributorExternalInfo getExternalDistributor() {
        return externalDistributor;
    }

    public void setExternalDistributor(DistributorExternalInfo externalDistributor) {
        this.externalDistributor = externalDistributor;
    }

    public OffsetDateTime getBoundAt() {
        return boundAt;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public void setBoundAt(OffsetDateTime boundAt) {
        this.boundAt = boundAt;
    }

}
