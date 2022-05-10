package com.apps.freeroadingdriver.model.responseModel

import android.os.Parcel
import android.os.Parcelable

class Booking_data() : Parcelable {

    var snowboard_or_skis: String? = null

    var booking_date: String? = null

    var customer_profile_pic: String? = null

    var trip_date: String? = null

    var driver_timezone: String? = null

    var stroller: String? = null

    var arrive_dt: String? = null

    var card_id: String? = null

    var app_appointment_id: String? = null

    var pick_address: String? = null

    var pets: String? = null

    var pick_latitude: String? = null

    var driver_id: String? = null

    var drop_address: String? = null

    var surf_board: String? = null

    var ride_id: String? = null

    var credit_amount: String? = null

    var total_amount: String? = null

    var status: String? = null

    var customer_email: String? = null

    var customer_name: String? = null
    var taxi_plate_no: String? = null

    var no_of_riders: String? = null

    var accepted_dt: String? = null

    var server_trip_datetime: String? = null

    var payment_token: String? = null

    var language: String? = null

    var driver_type: String? = null

    var started_dt: String? = null

    var online_payment_status: String? = null

    var distance_in_mts: String? = null

    var pick_longitude: String? = null

    var cancel_status: String? = null

    var customer_country_code: String? = null

    var admin_commission_percentage: String? = null

    var customer_type: String? = null

    var actual_amount: String? = null

    var payment_status: String? = null

    var cancel_dt: String? = null

    var bike: String? = null

    var distance_value: String? = null

    var trip_time: String? = null

    var admin_commission: String? = null

    var drop_longitude: String? = null

    var cancel_reason: String? = null

    var drop_latitude: String? = null

    var speed: String? = null

    var cash_amount: String? = null

    var customer_mobile: String? = null

    var created_dt: String? = null

    var complete_dt: String? = null

    var duration: String? = null

    var online_payment_transaction_id: String? = null

    var user_appointment_date: String? = null

    var customer_id: String? = null

    var appointment_timezone: String? = null

    var payment_type: String? = null

    var number_of_bags: String? = null

    var car_seat_booking:String?=null
    var dis:String?=null

    var booked_seat:String?=null

    constructor(parcel: Parcel) : this() {
        snowboard_or_skis = parcel.readString()
        booking_date = parcel.readString()
        customer_profile_pic = parcel.readString()
        trip_date = parcel.readString()
        driver_timezone = parcel.readString()
        stroller = parcel.readString()
        arrive_dt = parcel.readString()
        card_id = parcel.readString()
        app_appointment_id = parcel.readString()
        pick_address = parcel.readString()
        pets = parcel.readString()
        pick_latitude = parcel.readString()
        driver_id = parcel.readString()
        drop_address = parcel.readString()
        surf_board = parcel.readString()
        ride_id = parcel.readString()
        credit_amount = parcel.readString()
        total_amount = parcel.readString()
        status = parcel.readString()
        customer_email = parcel.readString()
        customer_name = parcel.readString()
        taxi_plate_no = parcel.readString()
        no_of_riders = parcel.readString()
        accepted_dt = parcel.readString()
        server_trip_datetime = parcel.readString()
        payment_token = parcel.readString()
        language = parcel.readString()
        driver_type = parcel.readString()
        started_dt = parcel.readString()
        online_payment_status = parcel.readString()
        distance_in_mts = parcel.readString()
        pick_longitude = parcel.readString()
        cancel_status = parcel.readString()
        customer_country_code = parcel.readString()
        admin_commission_percentage = parcel.readString()
        customer_type = parcel.readString()
        actual_amount = parcel.readString()
        payment_status = parcel.readString()
        cancel_dt = parcel.readString()
        bike = parcel.readString()
        distance_value = parcel.readString()
        trip_time = parcel.readString()
        admin_commission = parcel.readString()
        drop_longitude = parcel.readString()
        cancel_reason = parcel.readString()
        drop_latitude = parcel.readString()
        speed = parcel.readString()
        cash_amount = parcel.readString()
        customer_mobile = parcel.readString()
        created_dt = parcel.readString()
        complete_dt = parcel.readString()
        duration = parcel.readString()
        online_payment_transaction_id = parcel.readString()
        user_appointment_date = parcel.readString()
        customer_id = parcel.readString()
        appointment_timezone = parcel.readString()
        payment_type = parcel.readString()
        number_of_bags=parcel.readString()
        car_seat_booking=parcel.readString()
        dis=parcel.readString()
        booked_seat=parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(snowboard_or_skis)
        parcel.writeString(booking_date)
        parcel.writeString(customer_profile_pic)
        parcel.writeString(trip_date)
        parcel.writeString(driver_timezone)
        parcel.writeString(stroller)
        parcel.writeString(arrive_dt)
        parcel.writeString(card_id)
        parcel.writeString(app_appointment_id)
        parcel.writeString(pick_address)
        parcel.writeString(pets)
        parcel.writeString(pick_latitude)
        parcel.writeString(driver_id)
        parcel.writeString(drop_address)
        parcel.writeString(surf_board)
        parcel.writeString(ride_id)
        parcel.writeString(credit_amount)
        parcel.writeString(total_amount)
        parcel.writeString(status)
        parcel.writeString(customer_email)
        parcel.writeString(customer_name)
        parcel.writeString(taxi_plate_no)
        parcel.writeString(no_of_riders)
        parcel.writeString(accepted_dt)
        parcel.writeString(server_trip_datetime)
        parcel.writeString(payment_token)
        parcel.writeString(language)
        parcel.writeString(driver_type)
        parcel.writeString(started_dt)
        parcel.writeString(online_payment_status)
        parcel.writeString(distance_in_mts)
        parcel.writeString(pick_longitude)
        parcel.writeString(cancel_status)
        parcel.writeString(customer_country_code)
        parcel.writeString(admin_commission_percentage)
        parcel.writeString(customer_type)
        parcel.writeString(actual_amount)
        parcel.writeString(payment_status)
        parcel.writeString(cancel_dt)
        parcel.writeString(bike)
        parcel.writeString(distance_value)
        parcel.writeString(trip_time)
        parcel.writeString(admin_commission)
        parcel.writeString(drop_longitude)
        parcel.writeString(cancel_reason)
        parcel.writeString(drop_latitude)
        parcel.writeString(speed)
        parcel.writeString(cash_amount)
        parcel.writeString(customer_mobile)
        parcel.writeString(created_dt)
        parcel.writeString(complete_dt)
        parcel.writeString(duration)
        parcel.writeString(online_payment_transaction_id)
        parcel.writeString(user_appointment_date)
        parcel.writeString(customer_id)
        parcel.writeString(appointment_timezone)
        parcel.writeString(payment_type)
        parcel.writeString(number_of_bags)
        parcel.writeString(car_seat_booking)
        parcel.writeString(dis)
        parcel.writeString(booked_seat)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Booking_data> {
        override fun createFromParcel(parcel: Parcel): Booking_data {
            return Booking_data(parcel)
        }

        override fun newArray(size: Int): Array<Booking_data?> {
            return arrayOfNulls(size)
        }
    }
}
