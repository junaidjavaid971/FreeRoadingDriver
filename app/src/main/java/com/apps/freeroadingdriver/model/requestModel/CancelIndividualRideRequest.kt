package com.apps.freeroadingdriver.model.requestModel

class CancelIndividualRideRequest(val app_appointment_id : String,
                                  val request_date : String,
                                  val user_timezone : String,
                                  val cancel_reason : String,
                                  val cancelled_by : String) : BaseRequest(false,true,false,false) {
}