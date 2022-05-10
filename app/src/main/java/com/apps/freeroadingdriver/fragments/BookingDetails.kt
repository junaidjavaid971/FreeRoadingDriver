package com.app.freeroading.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.activities.DashboardActivity
import com.apps.freeroadingdriver.constants.CommonMethods
import com.apps.freeroadingdriver.model.responseModel.Road_trips
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_booking_details.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class BookingDetails : Fragment() {

    //lateinit var trip_details: Trip_details
    lateinit var road_trips: Road_trips

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            road_trips = requireArguments().getParcelable(ARG_PARAM1)!!
            // trip_details = road_trips.trip_details!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAllView()
    }

    fun setAllView() {
        (context as DashboardActivity).iv_details.visibility = View.GONE
        tv_pickup.setText(road_trips.pickup_location)
        tv_drop_off.setText(road_trips.droppoff_location)
        tv_departure_date.setText(road_trips.departure_date!!.split(" ").get(0))
        tv_departure_time.setText(road_trips.departure_date!!.split(" ").get(1))
        tv_car_make.setText(road_trips.car_make)
        tv_price_per_ride.setText("$ " + road_trips.how_much_ride_amount)
        tv_vehicle_year.setText(road_trips.vehicle_year)
        tv_color_vehicle.setText(road_trips.vehicle_color)
        tv_vehical_type.setText(road_trips.what_type_vehicle_have)
        tv_how_many_door.setText(road_trips.total_doors)
        tv_taxi_plate_num.setText(road_trips.taxi_plate_no)
        tv_total_rider.setText(road_trips.number_of_riders)
        tv_booked_rider.setText(road_trips.booked_seat)
        strollerNumb.text =
            CommonMethods.returnNotnullString(if (road_trips.stroller.equals("1")) "Yes" else "No")
        numofcarSeat.text =
            CommonMethods.returnNotnullString(if (road_trips.car_seat.equals("1")) "Yes" else "No")
        typebike.text =
            CommonMethods.returnNotnullString(if (road_trips.bike.equals("1")) "Yes" else "No")
        snowOrSki.text =
            CommonMethods.returnNotnullString(if (road_trips.snowboard_or_skis.equals("1")) "Yes" else "No")
        surfBoard.text =
            CommonMethods.returnNotnullString(if (road_trips.surf_board.equals("1")) "Yes" else "No")
        petsAllowed.text =
            CommonMethods.returnNotnullString(if (road_trips.pets.equals("1")) "Yes" else "No")

    }

    companion object {
        // TODO: Rename and change types and number of parameters
        val TAG = BookingDetails::class.java.name

        @JvmStatic
        fun newInstance(param1: Road_trips, param2: String) =
            BookingDetails().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}
