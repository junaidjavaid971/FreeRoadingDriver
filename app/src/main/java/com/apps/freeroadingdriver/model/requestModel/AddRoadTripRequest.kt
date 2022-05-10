package com.apps.freeroadingdriver.model.requestModel

/**
 * Created by Harshil on 3/30/2018.
 */
class AddRoadTripRequest(internal val pickup_location: String,
                         internal val droppoff_location: String,
                         internal val what_type_vehicle_have: String,
                         internal val car_make: String,
                         internal val vehicle_year: String,
                         internal val vehicle_color: String,
                         internal val total_doors: String,
                         internal val departure_date: String,
                         internal val departure_time: String,
                         internal val how_much_ride_amount: String,
                         internal val number_of_riders: String,
                         internal val stroller: String,
                         internal val car_seat: String,
                         internal val bike: String,
                         internal val snowboard_or_skis: String,
                         internal val surf_board: String,
                         internal val pets: String,
                         internal val is_ride_active: String,
                         internal val how_many_stroller: String,
                         internal val how_many_car_seat: String,
                         internal val how_many_bike: String,
                         internal val how_many_snowboard: String,
                         internal val how_many_surf_boards: String,
                         internal val how_many_pets: String,
                         internal val ride_pick_latitude: String,
                         internal val ride_pick_longitude: String,
                         internal val ride_drop_latitude: String,
                         internal val ride_drop_longitude: String,
                         internal val zipcode: String,
                         internal val city: String,
                         internal val drop_zipcode: String,
                         internal val drop_city: String,
                         internal val taxi_plate_no: String

                      ) : BaseRequest(false,true,false,false) {
}