package com.wangkang.goodwillghservice.feature.user.distributor;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;
import java.util.UUID;

public class TilerSalesDTO {

    @NotNull
    private UUID uuid;
    @NotEmpty
    private Collection<TilerSalesDataDTO> tilerSalesData;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Collection<TilerSalesDataDTO> getTilerSalesData() {
        return tilerSalesData;
    }

    public void setTilerSalesData(Collection<TilerSalesDataDTO> tilerSalesData) {
        this.tilerSalesData = tilerSalesData;
    }
}
