package com.apps.freeroadingdriver.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.constants.AppConstant
import com.apps.freeroadingdriver.constants.CommonMethods
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.model.responseModel.Home_data
import kotlinx.android.synthetic.main.fragment_ride_history_detail.*
import org.greenrobot.eventbus.Subscribe


/**
 * A simple [Fragment] subclass.
 * Use the [RideHIstoryDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RideHIstoryDetailFragment : BaseFragment() {

    // TODO: Rename and change types of parameters
    private var rideHistoryItem: Home_data? = null
    private var mParam2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            rideHistoryItem = requireArguments().getSerializable(ARG_PARAM1) as Home_data
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ride_history_detail, container, false)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RideHIstoryDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: Home_data, param2: String): RideHIstoryDetailFragment {
            val fragment = RideHIstoryDetailFragment()
            val args = Bundle()
            args.putSerializable(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateView()
    }

    @SuppressLint("SetTextI18n")
    private fun updateView() {
        dName.setText(rideHistoryItem?.customer_name)
        carNo.setText(rideHistoryItem?.vehicle_reg_no)
        bookingId.setText(getString(R.string.booking_id) + rideHistoryItem?.getApp_appointment_id())
        val datee = rideHistoryItem?.appointment_date?.split(" ")
        val app_date = datee?.get(0)
        date.setText(app_date)
        pick.setText(rideHistoryItem?.getPick_address())
        drop.setText(rideHistoryItem?.getDrop_address())
        // distance.setText(""+ CommonMethods.returnNotnullString(rideHistoryItem?.distance_in_mts)+AppConstant.DISTANCE_UNIT)
        distance.setText("" + String.format("%.2f", java.lang.Double.parseDouble(rideHistoryItem?.distance_in_mts)) + AppConstant.DISTANCE_UNIT)


        totalTIme.setText("" + CommonMethods.returnNotnullString(rideHistoryItem?.duration) + " Mins.")
        fare.setText(AppConstant.CURRENCY_UNIT + CommonMethods.returnNotnullString(rideHistoryItem?.driver_amount))


        if (!rideHistoryItem?.tip_amount.equals("0")) {

            tipAmount.visibility = View.VISIBLE;
            tip_label.visibility = View.VISIBLE;

            tipAmount.setText(AppConstant.CURRENCY_UNIT + rideHistoryItem?.tip_amount)

        } else {
            tipAmount.visibility = View.GONE;
            tip_label.visibility = View.GONE;
        }
    }

    @Subscribe
    override fun onEvent(eventObject: EventObject) {

    }

}// Required empty public constructor
