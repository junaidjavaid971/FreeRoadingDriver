package com.apps.freeroadingdriver.model.requestModel

class TripJourneyStartRequest(val ride_id : String,
                              val request_date : String,
                              val user_timezone : String,
                              val actual_pick_latitude : String,
                              val actual_pick_longitude : String):BaseRequest(false,true,false,false) {
}