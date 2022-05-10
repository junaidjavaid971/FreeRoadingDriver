package com.apps.freeroadingdriver.model.responseModel;

public class Status {
    private String profile_status;

    private String stripe_status;

    private String backend_status;

    private String admin_status;

    private String vehicle_status;

    public String getProfile_status ()
    {
        return profile_status;
    }

    public void setProfile_status (String profile_status)
    {
        this.profile_status = profile_status;
    }

    public String getStripe_status ()
    {
        return stripe_status;
    }

    public void setStripe_status (String stripe_status)
    {
        this.stripe_status = stripe_status;
    }

    public String getBackend_status ()
    {
        return backend_status;
    }

    public void setBackend_status (String backend_status)
    {
        this.backend_status = backend_status;
    }

    public String getAdmin_status ()
    {
        return admin_status;
    }

    public void setAdmin_status (String admin_status)
    {
        this.admin_status = admin_status;
    }

    public String getVehicle_status ()
    {
        return vehicle_status;
    }

    public void setVehicle_status (String vehicle_status)
    {
        this.vehicle_status = vehicle_status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [profile_status = "+profile_status+", stripe_status = "+stripe_status+", backend_status = "+backend_status+", admin_status = "+admin_status+", vehicle_status = "+vehicle_status+"]";
    }
}
