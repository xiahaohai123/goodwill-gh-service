package com.wangkang.goodwillghservice.feature.k3cloud.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Result {
    @JsonProperty("Id")
    private String id;
    @JsonProperty("NeedReturnData")
    private List<Object> needReturnData;
    @JsonProperty("ResponseStatus")
    private ResponseStatus responseStatus;

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Object> getNeedReturnData() {
        return needReturnData;
    }

    public void setNeedReturnData(List<Object> needReturnData) {
        this.needReturnData = needReturnData;
    }
}
