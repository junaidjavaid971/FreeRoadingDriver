package com.apps.freeroadingdriver.model.responseModel;

/**
 * Created by Krishna on 1/30/2018.
 */

public class Break_info
{
    private String id;

    private String updated_at;

    private String status;

    private String stop_time;

    private String created_at;

    private String break_duration;

    private String appointment_id;

    private String break_reason;

    private String resume_time;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getUpdated_at ()
    {
        return updated_at;
    }

    public void setUpdated_at (String updated_at)
    {
        this.updated_at = updated_at;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getStop_time ()
    {
        return stop_time;
    }

    public void setStop_time (String stop_time)
    {
        this.stop_time = stop_time;
    }

    public String getCreated_at ()
    {
        return created_at;
    }

    public void setCreated_at (String created_at)
    {
        this.created_at = created_at;
    }

    public String getBreak_duration ()
    {
        return break_duration;
    }

    public void setBreak_duration (String break_duration)
    {
        this.break_duration = break_duration;
    }

    public String getAppointment_id ()
    {
        return appointment_id;
    }

    public void setAppointment_id (String appointment_id)
    {
        this.appointment_id = appointment_id;
    }

    public String getBreak_reason ()
    {
        return break_reason;
    }

    public void setBreak_reason (String break_reason)
    {
        this.break_reason = break_reason;
    }

    public String getResume_time ()
    {
        return resume_time;
    }

    public void setResume_time (String resume_time)
    {
        this.resume_time = resume_time;
    }

}