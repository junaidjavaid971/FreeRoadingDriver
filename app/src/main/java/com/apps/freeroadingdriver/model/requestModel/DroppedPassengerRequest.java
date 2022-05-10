package com.apps.freeroadingdriver.model.requestModel;

/**
 * Created by Admin on 10/10/2017.
 */

public class DroppedPassengerRequest extends BaseRequest {
    private final String distance;
    private String app_appointment_id;
    private String request_date;
    private String driver_timezone;
    private String status;
    private String drop_latitude;
    private String drop_longitude;
    private final String drop_address;
    public DroppedPassengerRequest(String app_appointment_id, String request_date, String driver_timezone, String status, String drop_latitude, String drop_longitude, String drop_Address, String journeyDistanceInMeter) {
        super(false, true, false, false);
        this.app_appointment_id = app_appointment_id;
        this.request_date = request_date;
        this.driver_timezone = driver_timezone;
        this.status=status;
        this.distance=journeyDistanceInMeter;
        this.drop_latitude=drop_latitude;
        this.drop_longitude=drop_longitude;
        this.drop_address=drop_Address;
    }
}
