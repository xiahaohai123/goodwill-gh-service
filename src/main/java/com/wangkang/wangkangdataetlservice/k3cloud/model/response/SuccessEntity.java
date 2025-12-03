package com.wangkang.wangkangdataetlservice.k3cloud.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SuccessEntity {
    @JsonProperty("DIndex")
    private int index;
    @JsonProperty("Id")
    private String id;
    @JsonProperty("Number")
    private String number;

    // Getters and Setters
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
