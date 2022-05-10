package com.app.freeroading.model.requestModel

import com.apps.freeroadingdriver.model.requestModel.BaseRequest

class TripInvoiceRequest(val app_appointment_id:String,val user_type:String): BaseRequest(false,true,false,false) {
}