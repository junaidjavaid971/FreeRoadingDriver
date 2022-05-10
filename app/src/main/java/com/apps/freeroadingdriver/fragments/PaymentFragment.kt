package com.apps.freeroadingdriver.fragments
import android.app.AlertDialog
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.activities.BaseActivity
import com.apps.freeroadingdriver.activities.DashboardActivity
import com.apps.freeroadingdriver.constants.AppConstant
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.model.requestModel.FeedBackRequest
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import com.apps.freeroadingdriver.model.responseModel.Home_data
import com.apps.freeroadingdriver.prefrences.FreeRoadingPreferenceManager
import com.apps.freeroadingdriver.requester.BackgroundExecutor
import com.apps.freeroadingdriver.requester.FeedBackRequester
import com.apps.freeroadingdriver.utils.CommonUtil
import com.apps.freeroadingdriver.utils.DialogUtil
import com.apps.freeroadingdriver.utils.LocationUtil
import kotlinx.android.synthetic.main.fragment_payment.*
import kotlinx.android.synthetic.main.progress_bar_layout.*
import org.greenrobot.eventbus.Subscribe
class PaymentFragment : BaseFragment() {
    private var bookingId: String? = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        super.onAttach(context)
        if (context is BaseActivity) {
            (context as DashboardActivity).setToolbarTitle(getString(R.string.header_invoice))
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBroadCastReciever();
        if (arguments != null) {
            val responseData = requireArguments().getSerializable(KEY_TYPE) as? Home_data
            setDefaultView(responseData);

            btn_finish_ride.setOnClickListener(View.OnClickListener {

                if((driverRating.rating).toString().equals("0.0")){
                    DialogUtil.showToastLongLength(context, "Rating is mandatory")

                }else{
                    finishRide()

                }
            })
        }
    }

    private fun setDefaultView(responseData: Home_data?) {
        try {
            tv_invoice_price_value?.setText("$ " + responseData?.actual_amount!!)
            tv_invoice_servicecharge_value?.setText("$ " + responseData?.service_charge)
            tv_invoice_total_value?.setText("$ " + responseData?.total_amount!!)
            tv_invoice_waiting_charge_value.setText("$ "+responseData?.waiting_amount)
            tv_invoice_waiting_time.setText(responseData?.total_waiting_time)
            tv_payment_booking_id?.setText("Booking Id: " + responseData?.app_appointment_id!!)
            tv_invoice_ride_pickup_address_value?.setText(responseData?.pick_address)
            bookingId = responseData?.app_appointment_id

            if (responseData?.old_drop_off_address != null) {
                if (responseData?.old_drop_off_address!!.trim { it <= ' ' } != "") {
                    ll_ride_new_address?.setVisibility(View.VISIBLE)
                    tv_invoice_ride_new_address_value?.setText(responseData?.drop_address)
                    tv_invoice_ride_drop_address_value?.setText(responseData?.old_drop_off_address)
                } else {
                    tv_invoice_ride_drop_address_value?.setText(responseData?.drop_address)
                    ll_ride_new_address?.setVisibility(View.GONE)
                }
            } else {
                ll_ride_new_address?.setVisibility(View.GONE)
                tv_invoice_ride_drop_address_value?.setText(responseData?.drop_address)
            }

            tv_invoice_ride_duration_value?.setText("" + responseData?.duration)
            tv_invoice_ride_drop_distance_value?.setText("" + LocationUtil.getMilesFrmMeter(java.lang.Double.parseDouble(responseData?.distance_in_mts)) + AppConstant.DISTANCE_UNIT)
            tv_invoice_ride_wait_value?.setText("" + responseData?.speed)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    private var onRideRequestReciever: BroadcastReceiver? = null

    private fun initBroadCastReciever() {
        onRideRequestReciever = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val action = intent.getStringExtra("action")
                val message = intent.getStringExtra("message")

                if(action.equals("15") || action.equals("14") ){
                    val builder = AlertDialog.Builder(activity)
                    builder.setMessage(message)
                    builder.setPositiveButton("OK"){dialog, which ->
                        dialog.dismiss()
                    }
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }


            }
        }
    }
    override fun onResume() {
        super.onResume()
        val onRideRequestIntent = IntentFilter()
        onRideRequestIntent.addAction("invoice")
        requireActivity().registerReceiver(onRideRequestReciever, onRideRequestIntent)
    }

    fun finishRide() {
        CommonUtil.showProgressBar(rl_progress_bar)
        BackgroundExecutor.instance.execute(FeedBackRequester(FeedBackRequest(bookingId,review.text.toString(),driverRating.rating.toString())))
    }

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        Log.d(TAG, "onEvent method called")
        if (eventObject == null) {
            return
        }
        requireActivity().runOnUiThread {
            CommonUtil.hideProgressBar(rl_progress_bar)
            onHandleBaseEvent(eventObject)
            val response = eventObject.`object` as BaseResponse
            when (eventObject.id) {
                EventConstant.PAYMENT_SUCCESS_STATUS -> {
                    val notificationManager = requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.cancelAll()
                    (context as DashboardActivity).setEnableDisableDrawer(true)
                    FreeRoadingPreferenceManager.getInstance().rideStatus = "Off"
                    FreeRoadingPreferenceManager.getInstance().appointmentId = ""
                    FreeRoadingPreferenceManager.getInstance().rideActive = false
                    requireActivity().finish()
                    startActivity(DashboardActivity.createIntent(requireActivity()))
                }
                EventConstant.PAYMENT_ERROR_STATUS -> DialogUtil.showOkButtonDialog(context, "Alert", response.response_msg, response.response_key)
            }

        }
    }

    companion object {
        val TAG = PaymentFragment::class.java.simpleName
        private val KEY_TYPE = "response_data"

        fun getInstance(responseData: Home_data?): PaymentFragment {
            val fragment = PaymentFragment()
            val bundle = Bundle()
            bundle.putSerializable(KEY_TYPE, responseData)
            fragment.arguments = bundle
            return fragment
        }
    }

}// Required empty public constructor
