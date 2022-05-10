package com.apps.freeroadingdriver.model.responseModel;

public class Vehicle_model {
    private String vehicle_model_id;

    private String vehicle_model_title;

    private String vehicle_make_id;

    public String getVehicle_model_id() {
        return vehicle_model_id;
    }

    public void setVehicle_model_id(String vehicle_model_id) {
        this.vehicle_model_id = vehicle_model_id;
    }

    public String getVehicle_model_title() {
        return vehicle_model_title;
    }

    public void setVehicle_model_title(String vehicle_model_title) {
        this.vehicle_model_title = vehicle_model_title;
    }

    public String getVehicle_make_id() {
        return vehicle_make_id;
    }

    public void setVehicle_make_id(String vehicle_make_id) {
        this.vehicle_make_id = vehicle_make_id;
    }

    @Override
    public String toString() {
        return "ClassPojo [vehicle_model_id = " + vehicle_model_id + ", vehicle_model_title = " + vehicle_model_title + ", vehicle_make_id = " + vehicle_make_id + "]";
    }
}
