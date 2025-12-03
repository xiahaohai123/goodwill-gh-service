package com.wangkang.goodwillghservice.k3cloud.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SuccessMessage {
    @JsonProperty("DIndex")
    private int index;
    @JsonProperty("FieldName")
    private String fieldName;
    @JsonProperty("Message")
    private String message;

    // Getters and Setters
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
