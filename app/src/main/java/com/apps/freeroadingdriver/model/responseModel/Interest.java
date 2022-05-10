package com.apps.freeroadingdriver.model.responseModel;

public class Interest {
    private String added_date;

    private String name;

    private String id;

    private String status;

    public String getAdded_date ()
    {
        return added_date;
    }

    public void setAdded_date (String added_date)
    {
        this.added_date = added_date;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [added_date = "+added_date+", name = "+name+", id = "+id+", status = "+status+"]";
    }
}
