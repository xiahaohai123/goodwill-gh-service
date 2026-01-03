package com.wangkang.goodwillghservice.feature.user.manager;

import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.UUID;

public class AvailableSalesCalBaselineDTO {
    @NotNull
    private UUID userId;
    @NotNull
    private OffsetDateTime baseline;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public OffsetDateTime getBaseline() {
        return baseline;
    }

    public void setBaseline(OffsetDateTime baseline) {
        this.baseline = baseline;
    }
}
