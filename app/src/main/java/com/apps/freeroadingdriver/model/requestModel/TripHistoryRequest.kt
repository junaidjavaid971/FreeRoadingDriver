package com.apps.freeroadingdriver.model.requestModel

 class TripHistoryRequest(val start_dt:String,val end_dt:String,val passenger_type:String,val user_type :String): BaseRequest(false,true,false,false){

}