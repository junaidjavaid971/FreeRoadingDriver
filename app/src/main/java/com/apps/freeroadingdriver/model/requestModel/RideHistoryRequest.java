package com.apps.freeroadingdriver.model.requestModel;

/**
 * Created by Admin on 10/10/2017.
 */

public class RideHistoryRequest extends BaseRequest {
    private String start_dt;
    private String end_dt;

    public RideHistoryRequest(String start_dt, String end_dt) {
        super(false, true, false, false);
        this.start_dt = start_dt;
        this.end_dt = end_dt;
    }
}
