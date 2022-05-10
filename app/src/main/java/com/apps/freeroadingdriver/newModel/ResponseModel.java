package com.apps.freeroadingdriver.newModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseModel {

    @SerializedName("response_msg")
    @Expose
    private String responseMsg;
    @SerializedName("response_key")
    @Expose
    private Integer responseKey;
    @SerializedName("response_status")
    @Expose
    private Integer responseStatus;
    @SerializedName("response_invalid")
    @Expose
    private Integer responseInvalid;

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public Integer getResponseKey() {
        return responseKey;
    }

    public void setResponseKey(Integer responseKey) {
        this.responseKey = responseKey;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    public Integer getResponseInvalid() {
        return responseInvalid;
    }

    public void setResponseInvalid(Integer responseInvalid) {
        this.responseInvalid = responseInvalid;
    }
}