package com.apps.freeroadingdriver.newModel;

public class MyPojo
{
    private Response_data response_data;

    private String response_status;

    private String response_invalid;

    private String response_key;

    private String response_msg;

    public Response_data getResponse_data ()
    {
        return response_data;
    }

    public void setResponse_data (Response_data response_data)
    {
        this.response_data = response_data;
    }

    public String getResponse_status ()
    {
        return response_status;
    }

    public void setResponse_status (String response_status)
    {
        this.response_status = response_status;
    }

    public String getResponse_invalid ()
    {
        return response_invalid;
    }

    public void setResponse_invalid (String response_invalid)
    {
        this.response_invalid = response_invalid;
    }

    public String getResponse_key ()
    {
        return response_key;
    }

    public void setResponse_key (String response_key)
    {
        this.response_key = response_key;
    }

    public String getResponse_msg ()
    {
        return response_msg;
    }

    public void setResponse_msg (String response_msg)
    {
        this.response_msg = response_msg;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response_data = "+response_data+", response_status = "+response_status+", response_invalid = "+response_invalid+", response_key = "+response_key+", response_msg = "+response_msg+"]";
    }
}