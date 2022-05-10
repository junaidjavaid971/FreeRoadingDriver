package com.apps.freeroadingdriver.newModel;

import java.util.List;

import com.apps.freeroadingdriver.model.dataModel.Profile;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseData {

    @SerializedName("profile")
    @Expose
    private Profile profile;
    @SerializedName("session_token")
    @Expose
    private String sessionToken;
    @SerializedName("pub_chn")
    @Expose
    private String pubChn;
    @SerializedName("ser_chn")
    @Expose
    private String serChn;
    @SerializedName("sessionData")
    @Expose
    private List<Object> sessionData = null;
    @SerializedName("driver_wallet_status")
    @Expose
    private Integer driverWalletStatus;

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getPubChn() {
        return pubChn;
    }

    public void setPubChn(String pubChn) {
        this.pubChn = pubChn;
    }

    public String getSerChn() {
        return serChn;
    }

    public void setSerChn(String serChn) {
        this.serChn = serChn;
    }

    public List<Object> getSessionData() {
        return sessionData;
    }

    public void setSessionData(List<Object> sessionData) {
        this.sessionData = sessionData;
    }

    public Integer getDriverWalletStatus() {
        return driverWalletStatus;
    }

    public void setDriverWalletStatus(Integer driverWalletStatus) {
        this.driverWalletStatus = driverWalletStatus;
    }


}