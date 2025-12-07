package com.wangkang.goodwillghservice.feature.k3cloud.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ResponseStatus {
    @JsonProperty("ErrorCode")
    private String errorCode;
    @JsonProperty("IsSuccess")
    private boolean isSuccess;
    @JsonProperty("MsgCode")
    private int msgCode;
    @JsonProperty("Errors")
    private List<Error> errors;
    @JsonProperty("SuccessEntitys")
    private List<SuccessEntity> successEntities;
    @JsonProperty("SuccessMessages")
    private List<SuccessMessage> successMessages;

    // Getters and Setters
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        this.isSuccess = success;
    }

    public int getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(int msgCode) {
        this.msgCode = msgCode;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    public List<SuccessEntity> getSuccessEntities() {
        return successEntities;
    }

    public void setSuccessEntities(List<SuccessEntity> successEntities) {
        this.successEntities = successEntities;
    }

    public List<SuccessMessage> getSuccessMessages() {
        return successMessages;
    }

    public void setSuccessMessages(List<SuccessMessage> successMessages) {
        this.successMessages = successMessages;
    }
}
