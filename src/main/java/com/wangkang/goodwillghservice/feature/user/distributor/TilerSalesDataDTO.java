package com.wangkang.goodwillghservice.feature.user.distributor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TilerSalesDataDTO {
    @NotBlank
    private String code;
    @NotNull
    private Integer quantity;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
