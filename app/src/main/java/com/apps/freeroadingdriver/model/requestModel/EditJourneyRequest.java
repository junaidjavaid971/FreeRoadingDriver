package com.apps.freeroadingdriver.model.requestModel;

/**
 * Created by Admin on 10/10/2017.
 */

public class EditJourneyRequest extends BaseRequest {
    private String app_appointment_id;
    private String time;
    private String edit_type;
    private String time_type;
    private String break_id;
    private String new_dropoff_location;
    private String drop_latitude,drop_longitude;
    public EditJourneyRequest(String app_appointment_id, String edit_type, String time, String time_type, String new_dropoff_location, String drop_latitude, String drop_longitude, String break_id) {
        super(false, true, false, false);
        this.app_appointment_id = app_appointment_id;
        this.edit_type = edit_type;
        this.time = time;
        this.time_type=time_type;
        this.break_id=break_id;
        this.new_dropoff_location=new_dropoff_location;
        this.drop_latitude=drop_latitude;
        this.drop_longitude=drop_longitude;
    }
}
