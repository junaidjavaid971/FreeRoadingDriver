package com.apps.freeroadingdriver.model.responseModel
import android.os.Parcel
import android.os.Parcelable
class Road_trips() : Parcelable {
    var snowboard_or_skis : String? = null
    var car_make : String? = null

    var vehicle_color : String? = null

    var stroller : String? = null

    var ride_drop_longitude : String? = null

    var is_ride_active : String? = null

    var distance : String? = null

    var bike : String? = null

    var ride_id: String? = null
    var pick_address: String? = null
    var drop_address: String? = null
    var user_appointment_date: String? = null

    var how_many_surf_boards : String? = null

    var number_of_riders : String? = null

    var car_seat : String? = null

    var pets : String? = null

    var name : String? = null

    var droppoff_location : String? = null

    var vehicle_year : String? = null

    var driver_id : String? = null

    var profile_pic : String? = null

    var how_many_stroller : String? = null

    var what_type_vehicle_have : String? = null

    var surf_board : String? = null

    var ride_pick_latitude : String? = null

    var how_many_bike : String? = null

    var ride_pick_longitude : String? = null

    var how_many_car_seat : String? = null

    var departure_time : String? = null

    var how_many_pets : String? = null

    var total_doors : String? = null

    var email : String? = null

    var route : String? = null

    var how_many_snowboard : String? = null

    var ride_drop_latitude : String? = null

    var departure_date : String? = null

    var pickup_location : String? = null
    var driname : String? = null
    var cname : String? = null


    var driver_type : String? = null

    var customer_id : String? = null

    var how_much_ride_amount : String? = null

    var mobile : String? = null

    var id : String? = null

    var trip_status : String? = null

    var booking_data : ArrayList<Booking_data>? = null

    var taxi_plate_no:String?=null

    var booked_seat:String?=null

    constructor(parcel: Parcel) : this() {
        snowboard_or_skis = parcel.readString()
        car_make = parcel.readString()
        vehicle_color = parcel.readString()
        stroller = parcel.readString()
        ride_drop_longitude = parcel.readString()
        is_ride_active = parcel.readString()
        distance = parcel.readString()
        bike = parcel.readString()
        ride_id = parcel.readString()
        user_appointment_date = parcel.readString()
        pick_address = parcel.readString()
        drop_address = parcel.readString()
        how_many_surf_boards = parcel.readString()
        number_of_riders = parcel.readString()
        car_seat = parcel.readString()
        pets = parcel.readString()
        name = parcel.readString()
        droppoff_location = parcel.readString()
        vehicle_year = parcel.readString()
        driver_id = parcel.readString()
        profile_pic = parcel.readString()
        how_many_stroller = parcel.readString()
        what_type_vehicle_have = parcel.readString()
        surf_board = parcel.readString()
        ride_pick_latitude = parcel.readString()
        how_many_bike = parcel.readString()
        ride_pick_longitude = parcel.readString()
        how_many_car_seat = parcel.readString()
        departure_time = parcel.readString()
        how_many_pets = parcel.readString()
        total_doors = parcel.readString()
        email = parcel.readString()
        route = parcel.readString()
        how_many_snowboard = parcel.readString()
        ride_drop_latitude = parcel.readString()
        departure_date = parcel.readString()
        pickup_location = parcel.readString()
        driname = parcel.readString()
        cname = parcel.readString()
        driver_type = parcel.readString()
        customer_id = parcel.readString()
        how_much_ride_amount = parcel.readString()
        mobile = parcel.readString()
        id = parcel.readString()
        trip_status = parcel.readString()
        taxi_plate_no=parcel.readString()
        booking_data = parcel.readArrayList(null) as ArrayList<Booking_data>?
        booked_seat=parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(snowboard_or_skis)
        parcel.writeString(car_make)
        parcel.writeString(vehicle_color)
        parcel.writeString(stroller)
        parcel.writeString(ride_drop_longitude)
        parcel.writeString(is_ride_active)
        parcel.writeString(distance)
        parcel.writeString(bike)
        parcel.writeString(ride_id)
        parcel.writeString(user_appointment_date)
        parcel.writeString(pick_address)
        parcel.writeString(drop_address)

        parcel.writeString(how_many_surf_boards)
        parcel.writeString(number_of_riders)
        parcel.writeString(car_seat)
        parcel.writeString(pets)
        parcel.writeString(name)
        parcel.writeString(droppoff_location)
        parcel.writeString(vehicle_year)
        parcel.writeString(driver_id)
        parcel.writeString(profile_pic)
        parcel.writeString(how_many_stroller)
        parcel.writeString(what_type_vehicle_have)
        parcel.writeString(surf_board)
        parcel.writeString(ride_pick_latitude)
        parcel.writeString(how_many_bike)
        parcel.writeString(ride_pick_longitude)
        parcel.writeString(how_many_car_seat)
        parcel.writeString(departure_time)
        parcel.writeString(how_many_pets)
        parcel.writeString(total_doors)
        parcel.writeString(email)
        parcel.writeString(route)
        parcel.writeString(how_many_snowboard)
        parcel.writeString(ride_drop_latitude)
        parcel.writeString(departure_date)
        parcel.writeString(pickup_location)
        parcel.writeString(driname)
        parcel.writeString(cname)
        parcel.writeString(driver_type)
        parcel.writeString(customer_id)
        parcel.writeString(how_much_ride_amount)
        parcel.writeString(mobile)
        parcel.writeString(id)
        parcel.writeString(trip_status)
        parcel.writeList(booking_data)
        parcel.writeString(taxi_plate_no)
        parcel.writeString(booked_seat)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Road_trips> {
        override fun createFromParcel(parcel: Parcel): Road_trips {
            return Road_trips(parcel)
        }

        override fun newArray(size: Int): Array<Road_trips?> {
            return arrayOfNulls(size)
        }
    }
}