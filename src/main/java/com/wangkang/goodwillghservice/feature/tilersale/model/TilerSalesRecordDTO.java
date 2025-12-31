package com.wangkang.goodwillghservice.feature.tilersale.model;

import com.wangkang.goodwillghservice.dao.goodwillghservice.tilersale.model.TilerSalesRecordType;
import org.springframework.hateoas.server.core.Relation;

import java.time.OffsetDateTime;
import java.util.UUID;

@Relation(collectionRelation = "items", itemRelation = "item")
public class TilerSalesRecordDTO {
    /** 花色 / 产品编码 */
    private String productColor;

    /** 经销商 / 门店 / 用户 */
    private UUID distributorId;

    /** 瓦工 */
    private UUID tilerId;

    // ========= 数量与金额 =========
    private Integer quantity;

    // ========= 状态与来源 =========

    /** SALE 销售 / CANCEL 撤销 / ADJUSTMENT 调整 */
    private TilerSalesRecordType recordType;

    // ========= 时间维度 =========
    /** 实际成交时间 */
    private OffsetDateTime saleTime;


    public String getProductColor() {
        return productColor;
    }

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    public UUID getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(UUID distributorId) {
        this.distributorId = distributorId;
    }

    public UUID getTilerId() {
        return tilerId;
    }

    public void setTilerId(UUID tilerId) {
        this.tilerId = tilerId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public TilerSalesRecordType getRecordType() {
        return recordType;
    }

    public void setRecordType(TilerSalesRecordType recordType) {
        this.recordType = recordType;
    }

    public OffsetDateTime getSaleTime() {
        return saleTime;
    }

    public void setSaleTime(OffsetDateTime saleTime) {
        this.saleTime = saleTime;
    }
}
