package com.apps.freeroadingdriver.model.requestModel

/**
 * Created by Harshil on 3/12/2018.
 */

class StatisticsRequest(internal var request_date: String, internal var driver_timezone: String) : BaseRequest(false, true, false, true)
