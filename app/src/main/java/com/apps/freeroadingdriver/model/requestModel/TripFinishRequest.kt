package com.app.freeroading.model.requestModel

import com.apps.freeroadingdriver.model.requestModel.BaseRequest

data class TripFinishRequest(val ride_id : String,
                             val request_date : String,
                             val user_timezone : String,
                             val actual_drop_address : String,
                             val actual_drop_lat : String,
                             val actual_drop_lang : String): BaseRequest(false,true,false,false) {
}