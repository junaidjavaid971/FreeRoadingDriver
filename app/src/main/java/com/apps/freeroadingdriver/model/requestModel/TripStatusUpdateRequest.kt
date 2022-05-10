package com.app.freeroading.model.requestModel

import com.apps.freeroadingdriver.model.requestModel.BaseRequest

data class TripStatusUpdateRequest(val app_appointment_id : String,
                              val request_date : String,
                              val user_timezone : String,
                              val actual_pick_drop_address : String,
                              val actual_pick_drop_lat : String,
                              val actual_pick_drop_lang : String,
                              val status : String): BaseRequest(false,true,false,false) {
}