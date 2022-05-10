package com.apps.freeroadingdriver.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.constants.AppConstant
import com.apps.freeroadingdriver.constants.CommonMethods
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.manager.UserManager
import com.apps.freeroadingdriver.model.requestModel.CancelRequest
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import com.apps.freeroadingdriver.pubnub.PubNubManager
import com.apps.freeroadingdriver.requester.BackgroundExecutor
import com.apps.freeroadingdriver.requester.CancelRequester
import com.apps.freeroadingdriver.utils.CommonUtil
import com.apps.freeroadingdriver.utils.DialogUtil
import com.apps.freeroadingdriver.utils.FragmentFactory
import com.apps.freeroadingdriver.utils.GPSTracker
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import kotlinx.android.synthetic.main.fragment_cancel_request.*
import kotlinx.android.synthetic.main.progress_bar_layout.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.json.JSONException


/**
 * A simple [Fragment] subclass.
 * Use the [CancelRequestFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CancelRequestFragment : BaseFragment() {
    @Subscribe override fun onEvent(eventObject: EventObject) {
        if (eventObject == null) {
            return
        }
        requireActivity().runOnUiThread {
            try {
                CommonUtil.hideProgressBar(rl_progress_bar)
                onHandleBaseEvent(eventObject)
                val response = eventObject.`object` as BaseResponse
                when (eventObject.id) {
                    EventConstant.CANCEL_SUCCESS -> {
                        Log.d(TAG, "cancel success")
                        publishPubnub(GPSTracker.getLatitude(), GPSTracker.getLongitude(), GPSTracker.getBearing())
                        FragmentFactory.removedBack(context)
                        EventBus.getDefault().post(EventObject(EventConstant.CANCEL, response))
                    }
                    EventConstant.CANCEL_ERROR -> {
                        Log.d(TAG, "cancel error")
                        DialogUtil.showOkButtonDialog(context, getString(R.string.message), response.response_msg, response.response_key)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var cancelStatus = "8" // Cancelled by driver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cancel_request, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        submit.setOnClickListener(View.OnClickListener {
            CommonUtil.showProgressBar(rl_progress_bar)
            BackgroundExecutor.instance.execute(CancelRequester(CancelRequest(mParam1!!, cancelStatus, CommonMethods.getTimeZone(), CommonMethods.getCurrentDateString()
            , "",returnCancelReason())))
        })
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
         * @return A new instance of fragment CancelRequestFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): CancelRequestFragment {
            val fragment = CancelRequestFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    private fun publishPubnub(lat: Double, lng: Double, bearing: Float) {
        try {
            val mObject = JsonObject()
            mObject.addProperty("rq_tp", "5")
            mObject.addProperty("lt", lat)
            mObject.addProperty("lg", lng)
            mObject.addProperty("st", "1")
            mObject.addProperty("email", UserManager.getInstance().user.email)
            mObject.addProperty("mobile", UserManager.getInstance().user.mobile)
            mObject.addProperty("name", UserManager.getInstance().user.name)
            mObject.addProperty("appointment_id", mParam1)
            PubNubManager.getPubnubManager().publish(mObject, mParam2)
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun returnCancelReason() : String{
        var reason : String = clientnotshown.text.toString()
        if(driverReachLate.isChecked){
            reason = clientnotshown.text.toString()
        }else if (noContactDriver.isChecked){
            reason = clientCancel.text.toString()
        }else if (driverRequestToCancel.isChecked){
            reason = wrongAddress.text.toString()
        }else if (otherReason.isChecked){
            reason = otherReasonText.text.toString()
        }else if (donocanceltrip.isChecked){
            reason = doNotCancelTrip.text.toString()
        }else if (nolongerwait.isChecked){
            reason = cancelCanLonger.text.toString()
        }else if (cancelfee.isChecked){
            reason = cancelLong.text.toString()
        }
        return reason
    }
}// Required empty public constructor
