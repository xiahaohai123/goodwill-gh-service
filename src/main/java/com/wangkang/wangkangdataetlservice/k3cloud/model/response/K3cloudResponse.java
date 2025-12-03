package com.wangkang.wangkangdataetlservice.k3cloud.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class K3cloudResponse {

    @JsonProperty("Result")
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
