package com.wangkang.goodwillghservice.feature.user.distributor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TilerSalesDataDTO {
    /** 花色编码 */
    @NotBlank
    private String colorCode;
    @NotNull
    @Positive
    private Integer quantity;

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
