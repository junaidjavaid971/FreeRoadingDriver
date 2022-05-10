package com.apps.freeroadingdriver.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.freeroading.model.requestModel.InvoicePayTripAmout
import com.app.freeroading.model.requestModel.TripInvoiceRequest
import com.app.freeroading.requester.SendFeedBackRoadTripRequester
import com.app.freeroading.requester.TripInvoiceRequester
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.activities.DashboardActivity
import com.apps.freeroadingdriver.constants.CommonMethods
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import com.apps.freeroadingdriver.model.responseModel.ResponseData
import com.apps.freeroadingdriver.requester.BackgroundExecutor
import com.apps.freeroadingdriver.utils.CommonUtil
import com.apps.freeroadingdriver.utils.DialogUtil
import kotlinx.android.synthetic.main.fragment_trip_invoice.*
import kotlinx.android.synthetic.main.progress_bar_layout.*
import org.greenrobot.eventbus.Subscribe

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TripInvoiceFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class TripInvoiceFragment : BaseFragment() {
    @Subscribe override fun onEvent(eventObject: EventObject) {
        if (eventObject == null) {
            return
        }
        requireActivity().runOnUiThread(Runnable {
            try {
                CommonUtil.hideProgressBar(rl_progress_bar)
                onHandleBaseEvent(eventObject)
                val response = eventObject.`object` as BaseResponse
                when (eventObject.id) {
                    EventConstant.TRIPINVOICE_SUCCESS -> {
                        Log.d(BaseFragment.TAG, "invoice success")
                        setDefaultView(response.response_data!!)
                    }
                    EventConstant.INVOICEPAYAMOUNT_SUCCESS->{
                        Log.d(BaseFragment.TAG, "success")
                        CommonMethods.showLongToast(context, response.response_msg)
                        (context as DashboardActivity).onBackPressed()
                        (context as DashboardActivity).onBackPressed()
                    }
                    EventConstant.TRIPINVOICE_ERROR,EventConstant.INVOICEPAYAMOUNT_ERROR-> {
                        val response = eventObject.`object` as BaseResponse
                        Log.d(BaseFragment.TAG, "cancel error")
                        DialogUtil.showOkButtonDialog(context, getString(R.string.message), response.response_msg, response.response_key)
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setDefaultView(responseData: ResponseData) {
        try {
            tv_invoice_price_value.setText("$ " + responseData.ride_appointment?.actualAmount)
            tv_invoice_servicecharge_value.setText("$ " + responseData.ride_appointment?.serviceCharge)
            tv_invoice_total_value.setText("$ " + responseData.ride_appointment?.totalAmount)
            tv_payment_booking_id.setText("Booking Id: " + responseData.ride_appointment?.appAppointmentId)
            tv_invoice_ride_pickup_address_value.setText(responseData.ride_appointment?.pickAddress)
            tv_invoice_ride_drop_address_value.setText(responseData.ride_appointment?.dropAddress)
            userType = responseData.current_user_type
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    // TODO: Rename and change types of parameters
    private var app_appointment_id: String? = null
    private var driverType: String? = null
    private var userType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            app_appointment_id = it.getString(ARG_PARAM1)
            driverType = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trip_invoice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CommonUtil.showProgressBar(rl_progress_bar)
        BackgroundExecutor.instance.execute(TripInvoiceRequester(TripInvoiceRequest(app_appointment_id!!,driverType!!)))

        btn_finish_ride.setOnClickListener(View.OnClickListener {
            if(driverRating.rating!=0.0f) {
                CommonUtil.showProgressBar(rl_progress_bar)
                    BackgroundExecutor.instance.execute(SendFeedBackRoadTripRequester(InvoicePayTripAmout(app_appointment_id!!, CommonMethods.getCurrentDateOnly(), CommonMethods.getTimeZone(),
                            driverRating.rating, "1")))
            }else{
                CommonMethods.showAlert(context,"Alert","Please give rating!")
            }
        })


    }





    companion object {
        val TAG: String = TripInvoiceFragment::class.java.name

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TripInvoiceFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                TripInvoiceFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
