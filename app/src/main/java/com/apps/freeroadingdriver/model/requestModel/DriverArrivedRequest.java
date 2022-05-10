package com.apps.freeroadingdriver.model.requestModel;

/**
 * Created by Admin on 10/10/2017.
 */

public class DriverArrivedRequest extends BaseRequest {
    private String app_appointment_id;
    private String request_date;
    private String driver_timezone;
    private String status;
    public DriverArrivedRequest(String app_appointment_id, String request_date, String driver_timezone, String status) {
        super(false, true, false, false);
        this.app_appointment_id = app_appointment_id;
        this.request_date = request_date;
        this.driver_timezone = driver_timezone;
        this.status=status;
    }
}
