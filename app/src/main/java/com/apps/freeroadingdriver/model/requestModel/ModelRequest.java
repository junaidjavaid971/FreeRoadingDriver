package com.apps.freeroadingdriver.model.requestModel;

/**
 * Created by Admin on 10/10/2017.
 */

public class ModelRequest extends BaseRequest {
    private String vehicle_make_id;

    public ModelRequest(String vehicle_make_id) {
        super(false, false, false, false);
        this.vehicle_make_id = vehicle_make_id;
    }
}
