package com.wangkang.goodwillghservice.feature.user.distributor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;
import java.util.UUID;

public class TilerSalesDTO {

    @NotNull
    private UUID tilerUuid;
    @NotEmpty
    @Valid
    private Collection<TilerSalesDataDTO> tilerSalesData;

    public UUID getTilerUuid() {
        return tilerUuid;
    }

    public void setTilerUuid(UUID tilerUuid) {
        this.tilerUuid = tilerUuid;
    }

    public Collection<TilerSalesDataDTO> getTilerSalesData() {
        return tilerSalesData;
    }

    public void setTilerSalesData(Collection<TilerSalesDataDTO> tilerSalesData) {
        this.tilerSalesData = tilerSalesData;
    }
}
