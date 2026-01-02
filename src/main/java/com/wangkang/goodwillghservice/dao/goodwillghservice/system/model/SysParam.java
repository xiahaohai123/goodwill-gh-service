package com.wangkang.goodwillghservice.dao.goodwillghservice.system.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "sys_param")
public class SysParam {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "param_key", nullable = false, unique = true)
    private String paramKey;

    /**
     * 这里直接映射为 String。
     * 由于数据库是 JSONB，Hibernate 会自动处理 JSON 解析。
     * 我们在 Service 层通过 ObjectMapper 转成具体的类型。
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "param_value", columnDefinition = "jsonb", nullable = false)
    private String paramValue;

    @Column(name = "value_type")
    private String valueType;

    private String description;

    @Column(name = "update_time")
    private OffsetDateTime updateTime;

    public SysParam() {
    }

    public SysParam(UUID id,
                    String paramKey,
                    String paramValue,
                    String valueType,
                    String description,
                    OffsetDateTime updateTime) {
        this.id = id;
        this.paramKey = paramKey;
        this.paramValue = paramValue;
        this.valueType = valueType;
        this.description = description;
        this.updateTime = updateTime;
    }

    @PreUpdate
    @PrePersist
    public void updateTimestamp() {
        this.updateTime = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OffsetDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(OffsetDateTime updateTime) {
        this.updateTime = updateTime;
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private String paramKey;
        private String paramValue;
        private String valueType;
        private String description;
        private OffsetDateTime updateTime;

        Builder() {
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder paramKey(String paramKey) {
            this.paramKey = paramKey;
            return this;
        }

        public Builder paramValue(String paramValue) {
            this.paramValue = paramValue;
            return this;
        }

        public Builder valueType(String valueType) {
            this.valueType = valueType;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder updateTime(OffsetDateTime updateTime) {
            this.updateTime = updateTime;
            return this;
        }

        public SysParam build() {
            return new SysParam(id, paramKey, paramValue, valueType, description, updateTime);
        }
    }
}