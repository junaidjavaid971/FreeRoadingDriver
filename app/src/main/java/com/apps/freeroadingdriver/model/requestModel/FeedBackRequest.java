package com.apps.freeroadingdriver.model.requestModel;

/**
 * Created by Admin on 10/10/2017.
 */

public class FeedBackRequest extends BaseRequest {
    private String app_appointment_id;
    private String review;
    private String rating;
    public FeedBackRequest(String app_appointment_id,String review,String rating) {
        super(false, true, false, false);
        this.app_appointment_id = app_appointment_id;
        this.review=review;
        this.rating=rating;
    }
}
