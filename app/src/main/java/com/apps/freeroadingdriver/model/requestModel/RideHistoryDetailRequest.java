package com.apps.freeroadingdriver.model.requestModel;

/**
 * Created by Admin on 10/10/2017.
 */

public class RideHistoryDetailRequest extends BaseRequest {
    private String app_appointment_id;

    public RideHistoryDetailRequest(String app_appointment_id) {
        super(false, true, false, false);
        this.app_appointment_id = app_appointment_id;
    }

}
