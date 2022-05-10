package com.apps.freeroadingdriver.model.requestModel

class CancelRoadTripRequest(val ride_id : String, val request_date : String, val user_timezone :String):BaseRequest(false,true,false,false) {
}