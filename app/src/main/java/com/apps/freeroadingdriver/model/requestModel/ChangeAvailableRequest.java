package com.apps.freeroadingdriver.model.requestModel;

/**
 * Created by Harshil on 12/20/2017.
 */

public class ChangeAvailableRequest extends BaseRequest {
    String status;

    public ChangeAvailableRequest(String status) {
        super(false, true, false, false);
        this.status = status;
    }
}
