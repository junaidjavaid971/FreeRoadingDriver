package com.app.freeroading.model.requestModel

import com.apps.freeroadingdriver.model.requestModel.BaseRequest

class InvoicePayTripAmout(val app_appointment_id: String,
                          val request_date: String,
                          val user_timezone: String,
                          val rating: Float,
                          val user_type : String): BaseRequest(false,true,false,false) {
}