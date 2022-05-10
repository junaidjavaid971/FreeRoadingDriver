package com.apps.freeroadingdriver.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.constants.AppConstant
import com.apps.freeroadingdriver.constants.CommonMethods
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.model.responseModel.RideHistory
import com.apps.freeroadingdriver.model.responseModel.Road_trips
import com.apps.freeroadingdriver.fragments.BaseFragment
import kotlinx.android.synthetic.main.fragment_trip_history_details.*
import org.greenrobot.eventbus.Subscribe

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class TripHistoryDetailsFragment : BaseFragment() {
    @Subscribe
    override fun onEvent(eventObject: EventObject) {
    }

    // TODO: Rename and change types of parameters
    private var rideHistoryItem: Road_trips? = null
    private var mParam2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            rideHistoryItem = requireArguments().getParcelable(TripHistoryDetailsFragment.ARG_PARAM1) as Road_trips?
            mParam2 = requireArguments().getString(TripHistoryDetailsFragment.ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trip_history_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mParam2=="2"){
            l_driver.visibility=View.VISIBLE
            l_passenger.visibility=View.GONE
            updateViewDriver()
        }
        else{
            l_passenger.visibility=View.VISIBLE
            l_driver.visibility=View.GONE
            updateView()
        }
    }

    private fun updateView() {
        dName.setText(rideHistoryItem!!.booking_data!!.get(0)?.customer_name)
        carNo.setText(rideHistoryItem!!.booking_data!!.get(0)?.taxi_plate_no)
        bookingId.setText(getString(R.string.booking_id) + rideHistoryItem!!.booking_data!!.get(0)?.ride_id)
        val datee = rideHistoryItem!!.booking_data!!.get(0)?.user_appointment_date?.split(" ")
        val app_date = datee?.get(0)
        date.setText(app_date)
        pick.setText(rideHistoryItem!!.booking_data!!.get(0)?.pick_address)
        drop.setText(rideHistoryItem!!.booking_data!!.get(0)?.drop_address)
        tv_price_value.setText(AppConstant.CURRENCY_UNIT + CommonMethods.returnNotnullString(rideHistoryItem?.how_much_ride_amount))
       // tv_service_charge_value.setText(AppConstant.CURRENCY_UNIT + CommonMethods.returnNotnullString(rideHistoryItem?.how_much_ride_amount))
        tv_service_charge_value.setText(AppConstant.CURRENCY_UNIT + "0")
        tv_total_value.setText(AppConstant.CURRENCY_UNIT + CommonMethods.returnNotnullString(rideHistoryItem?.how_much_ride_amount))

        /*if(!rideHistoryItem?.tip_amount.equals("0") and (rideHistoryItem?.tip_amount!=null)){
            tv_tip_value.setText(AppConstant.CURRENCY_UNIT + CommonMethods.returnNotnullString(rideHistoryItem?.tipsAmount))

        }else{
            ll_tip.visibility=View.GONE
        }
        if (!rideHistoryItem?.getWaiting_amount().equals("0")) {
            tv_waiting_charge.setVisibility(View.VISIBLE)
            tv_waiting_charge_value.setVisibility(View.VISIBLE)
            tv_waiting_charge_value.setText(AppConstant.CURRENCY_UNIT + CommonMethods.returnNotnullString(rideHistoryItem?.getWaiting_amount()))
        }*/
    }
    private fun updateViewDriver() {
        dNameDriver.setText(rideHistoryItem!!.name)
        txt_name_passenger.setText(rideHistoryItem?.booking_data!![0].customer_name)
        carNoDriver.setText(rideHistoryItem!!.booking_data!!.get(0)?.taxi_plate_no)
        bookingIdDriver.setText(getString(R.string.booking_id) + rideHistoryItem!!.booking_data!!.get(0)?.ride_id)
        bookingId_passenger.setText(getString(R.string.booking_id) + rideHistoryItem!!.booking_data!!.get(0)?.app_appointment_id)
        val datee = rideHistoryItem!!.booking_data!!.get(0)?.user_appointment_date?.split(" ")
        val app_date = datee?.get(0)
        dateDriver.setText(app_date)
        pickDriver.setText(rideHistoryItem!!.pickup_location)
        dropDriver.setText(rideHistoryItem!!.droppoff_location)
        pick_loc_passenger.setText(rideHistoryItem!!.booking_data!!.get(0)?.pick_address)
        drop_loc_passenger.setText(rideHistoryItem!!.booking_data!!.get(0)?.drop_address)
        fare.setText(AppConstant.CURRENCY_UNIT + CommonMethods.returnNotnullString(rideHistoryItem?.booking_data!![0].total_amount))
        tipAmount.setText(AppConstant.CURRENCY_UNIT + CommonMethods.returnNotnullString(rideHistoryItem?.booking_data!![0].actual_amount))
        totalTIme.setText(CommonMethods.returnNotnullString(rideHistoryItem?.booking_data!![0].trip_time))
        distance.setText(CommonMethods.returnNotnullString(rideHistoryItem?.booking_data!![0].dis)+" "+AppConstant.DISTANCE_UNIT)
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: Road_trips, param2: String) =
                TripHistoryDetailsFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
