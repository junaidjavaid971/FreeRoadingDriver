package com.apps.freeroadingdriver.model.responseModel
import com.apps.freeroadingdriver.model.dataModel.Profile
import java.io.Serializable
class ResponseData : Serializable {
    var otp: String? = null
    var profile: Profile? = null
    var session_token: String? = null
    var customer_id: String? = null
    var driver_id: String? = null
    var passenger_device_id: String? = null
    var app_appointment_id: String? = null
    val statistics: Statistics? = null
    val car_details: Car_details? = null
    val home_data: Home_data? = null
    var pick_address: String? = null
    var pick_latitude: String? = null
    var pick_longitude: String? = null
    var distance_in_mts: String? = null
    var current_user_type: String? = null
    var pub_chn: String? = null
    var ser_chn: String? = null
    val appointment: java.util.ArrayList<Home_data>? = null
    val page: Page? = null
    val break_info: Break_info? = null
    val ride_appointment: Ride_appointment? = null
    var customer_name: String? = null
    var mobile: String? = null
    var customer_email: String? = null
    var profile_pic: String? = null
    var drop_address: String? = null
    var check_status: Status? = null
    var drop_latitude: String? = null
    var drop_longitude: String? = null
    var actual_amount: String? = null
    var service_charge: String? = null
    var total_amount: String? = null
    var duration: String? = null
    var speed: String? = null
    var old_drop_off_address: String? = null
    var status: String? = null
    var waiting_amount: String? = null
    var total_waiting_time: String? = null
    val road_trips: ArrayList<Road_trips>? = null
    internal var booking_data: ArrayList<Booking_data>? = null
    var vehicle_make: Array<Vehicle_make>? = null
    var vehicle_type: Array<Vehicle_type>? = null
    var vehicle_model: Array<Vehicle_model>? = null
    var interest: Array<Interest>? = null


}
