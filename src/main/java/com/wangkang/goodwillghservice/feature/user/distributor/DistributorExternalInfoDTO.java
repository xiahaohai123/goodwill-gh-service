package com.wangkang.goodwillghservice.feature.user.distributor;

import org.springframework.hateoas.server.core.Relation;

import java.time.OffsetDateTime;
import java.util.UUID;

@Relation(collectionRelation = "items", itemRelation = "item")
public class DistributorExternalInfoDTO {
    private UUID id;
    private String externalName;
    private String externalCode;
    private OffsetDateTime syncedAt;
    /** 如果已绑定，则携带绑定的用户展示名 */
    private String userDisplayName;

    public DistributorExternalInfoDTO() {
    }

    public DistributorExternalInfoDTO(UUID id,
                                      String externalName,
                                      String externalCode,
                                      OffsetDateTime syncedAt,
                                      String userDisplayName) {
        this.id = id;
        this.externalName = externalName;
        this.externalCode = externalCode;
        this.syncedAt = syncedAt;
        this.userDisplayName = userDisplayName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getExternalName() {
        return externalName;
    }

    public void setExternalName(String externalName) {
        this.externalName = externalName;
    }

    public String getExternalCode() {
        return externalCode;
    }

    public void setExternalCode(String externalCode) {
        this.externalCode = externalCode;
    }

    public OffsetDateTime getSyncedAt() {
        return syncedAt;
    }

    public void setSyncedAt(OffsetDateTime syncedAt) {
        this.syncedAt = syncedAt;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }
}
