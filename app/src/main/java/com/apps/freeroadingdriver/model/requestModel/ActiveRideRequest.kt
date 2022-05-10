package com.apps.freeroadingdriver.model.requestModel

data class ActiveRideRequest(
        internal val departure_date : String,
        internal val user_type : String) : BaseRequest(false,true,false,false) {
}