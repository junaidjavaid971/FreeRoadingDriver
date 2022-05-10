package com.apps.freeroadingdriver.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.model.responseModel.Booking_data
import com.apps.freeroadingdriver.network.URLConstant
import com.apps.freeroadingdriver.utils.GlideUtil
import kotlinx.android.synthetic.main.fragment_customer__details.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Customer_Details : Fragment() {


    var booking_data: Booking_data? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        var view = inflater.inflate(R.layout.fragment_customer__details, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDataOnView()
    }

    fun setDataOnView() {
        tv_name.setText(booking_data!!.customer_name)
        tv_booking_id.setText("Booking ID : " + booking_data!!.app_appointment_id)
        tv_pick_up_address.setText(booking_data!!.pick_address)
        tv_drop_off_location.setText(booking_data!!.drop_address)
        tv_distance.setText(booking_data!!.dis + " Miles")
        tv_departure_date.setText(booking_data!!.user_appointment_date!!.split(" ").get(0))
        tv_departure_time.setText(booking_data!!.trip_time)
        tv_party_number.setText(booking_data!!.no_of_riders)
        tv_stollers.setText(if (booking_data!!.stroller.equals("1")) "Yes" else "No")
        tv_bikes.setText(if (booking_data!!.bike.equals("1")) "Yes" else "No")
        tv_snowboard_skis.setText((if (booking_data!!.snowboard_or_skis.equals("1")) "Yes" else "No"))
        tv_surf_board.setText(if (booking_data!!.surf_board.equals("1")) "Yes" else "No")
        tv_pets.setText(if (booking_data!!.pets.equals("1")) "Yes" else "No")
        tv_bags.setText(booking_data!!.number_of_bags)
        tv_carseat.setText(if (booking_data!!.car_seat_booking.equals("1")) "Yes" else "No")
        GlideUtil.loadCircleImage(context, iv_profile, URLConstant.PASSENGER_IMAGE_URL + booking_data!!.customer_profile_pic!!, R.drawable.ic_profile_placeholder)

        iv_call.setOnClickListener(View.OnClickListener {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:${booking_data!!.customer_mobile}")
            startActivity(callIntent)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            booking_data = it.getParcelable<Booking_data>(ARG_PARAM1)
        }
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        val TAG = Customer_Details::class.java.name

        @JvmStatic
        fun newInstance(param1: Booking_data, param2: String) =
                Customer_Details().apply {
                    arguments = Bundle().apply {
                        putParcelable(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

}
