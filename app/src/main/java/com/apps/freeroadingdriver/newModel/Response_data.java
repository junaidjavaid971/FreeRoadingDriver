package com.apps.freeroadingdriver.newModel;

import com.apps.freeroadingdriver.model.dataModel.Profile;

public class Response_data
{
    private String session_token;

    private String ser_chn;

    private String pub_chn;

    private Profile profile;

    private String driver_wallet_status;

    public String getSession_token ()
    {
        return session_token;
    }

    public void setSession_token (String session_token)
    {
        this.session_token = session_token;
    }

    public String getSer_chn ()
    {
        return ser_chn;
    }

    public void setSer_chn (String ser_chn)
    {
        this.ser_chn = ser_chn;
    }

    public String getPub_chn ()
    {
        return pub_chn;
    }

    public void setPub_chn (String pub_chn)
    {
        this.pub_chn = pub_chn;
    }

    public Profile getProfile ()
    {
        return profile;
    }

    public void setProfile (Profile profile)
    {
        this.profile = profile;
    }

    public String getDriver_wallet_status ()
    {
        return driver_wallet_status;
    }

    public void setDriver_wallet_status (String driver_wallet_status)
    {
        this.driver_wallet_status = driver_wallet_status;
    }

    @Override
    public String toString()
    {
        //return "ClassPojo [session_token = "+session_token+", ser_chn = "+ser_chn+", pub_chn = "+pub_chn+",  driver_wallet_status = "+driver_wallet_status+"]";
        return "ClassPojo [session_token = "+session_token+", ser_chn = "+ser_chn+", pub_chn = "+pub_chn+", profile = "+profile+", driver_wallet_status = "+driver_wallet_status+"]";
    }
}