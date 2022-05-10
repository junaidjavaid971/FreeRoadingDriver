package com.apps.freeroadingdriver.model.responseModel;

public class Vehicle_make {
    private String vehicle_make_title;

    private String vehicle_make_id;

    public String getVehicle_make_title ()
    {
        return vehicle_make_title;
    }

    public void setVehicle_make_title (String vehicle_make_title)
    {
        this.vehicle_make_title = vehicle_make_title;
    }

    public String getVehicle_make_id ()
    {
        return vehicle_make_id;
    }

    public void setVehicle_make_id (String vehicle_make_id)
    {
        this.vehicle_make_id = vehicle_make_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [vehicle_make_title = "+vehicle_make_title+", vehicle_make_id = "+vehicle_make_id+"]";
    }
}
