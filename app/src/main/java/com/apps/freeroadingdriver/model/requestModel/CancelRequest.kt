package com.apps.freeroadingdriver.model.requestModel

/**
 * Created by Harshil on 1/18/2018.
 */

class CancelRequest(internal var app_appointment_id: String, internal var cancel_status: String, internal var driver_timezone: String,
                    internal var request_date: String, internal var estimated_duration: String, internal var cancel_reason: String) :
        BaseRequest(false, true, false, false)
