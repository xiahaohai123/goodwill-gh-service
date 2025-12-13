package com.wangkang.goodwillghservice.feature.user.manager;


import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class BindDistributorDTO {
    @NotNull
    private UUID userId;
    @NotNull
    private UUID externalId;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getExternalId() {
        return externalId;
    }

    public void setExternalId(UUID externalId) {
        this.externalId = externalId;
    }
}
