package com.apps.freeroadingdriver.fragments


import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.activities.BaseActivity
import com.apps.freeroadingdriver.activities.DashboardActivity
import com.apps.freeroadingdriver.constants.CommonMethods
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.model.requestModel.EditJourneyRequest
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import com.apps.freeroadingdriver.prefrences.FreeRoadingPreferenceManager
import com.apps.freeroadingdriver.requester.BackgroundExecutor
import com.apps.freeroadingdriver.requester.EditJourneyRequester
import com.apps.freeroadingdriver.utils.CommonUtil
import com.apps.freeroadingdriver.utils.DialogUtil
import kotlinx.android.synthetic.main.fragment_edit_journey.*
import kotlinx.android.synthetic.main.progress_bar_layout.*
import org.greenrobot.eventbus.Subscribe


/**
 * A simple [Fragment] subclass.
 * Use the [EditJourneyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditJourneyFragment : BaseFragment(),ChangeDestinationFragment.EditJourneyResponse {
    @Subscribe override fun onEvent(eventObject: EventObject) {
        if (eventObject==null){
            return
        }
        requireActivity().runOnUiThread {
            try {
                CommonUtil.hideProgressBar(rl_progress_bar)
                onHandleBaseEvent(eventObject)
                val response = eventObject.`object` as BaseResponse
                when (eventObject.id) {
                    EventConstant.EDIT_JOURNEY_SUCCESS -> {
                        FreeRoadingPreferenceManager.getInstance().appointmentId = mParam1
                        breakId = response.response_data!!.break_info!!.getId()
                    }
                    EventConstant.EDIT_JOURNEY_ERROR -> {
                        DialogUtil.showOkButtonDialog(context,"Alert", response.response_msg,response.response_key)
                    }
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var timerData = ""
    private var startDateAndTime = ""
    private var timerStatus = "Off"
    private var breakId = ""
    internal var bundleData: Bundle? = null
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
        return inflater.inflate(R.layout.fragment_edit_journey, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_stop_request.setOnClickListener(View.OnClickListener {
            stopRequest()
        })

        btn_resume_request.setOnClickListener(View.OnClickListener {
            resumeRequest()
        })

        changeDestination.setOnClickListener(View.OnClickListener {
            changeDestination()
        })
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        val TAG = EditJourneyFragment::class.java.name
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditJourneyFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): EditJourneyFragment {
            val fragment = EditJourneyFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            FreeRoadingPreferenceManager.getInstance().setRideStatus("Edit")
            return fragment
        }
    }

    private fun setDefaultTimer() {
        if (timeSwapBuff != 0L) {
            /* btn_stop_request.setVisibility(View.GONE);
            btn_resume_request.setVisibility(View.GONE);*/
            tv_timer.setText(timerData)
        }
    }

    private var startTime = 0L
    private val customHandler = Handler()

    internal var timeInMilliseconds = 0L
    internal var timeSwapBuff = 0L
    internal var updatedTime = 0L
    private val updateTimerThread = object : Runnable {
        override fun run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime
            updatedTime = timeSwapBuff + timeInMilliseconds

            var seconds = (updatedTime / 1000).toInt()
            val minutes = seconds / 60
            val hours = minutes / 60
            seconds = seconds % 60
            val milliseconds = (updatedTime % 1000).toInt()

            var string = ""
            string += "" + String.format("%02d", hours)
            string += ":" + String.format("%02d", minutes)
            string += ":" + String.format("%02d", seconds)
            // string += ":" + String.format("%03d", milliseconds);

            requireActivity().runOnUiThread(Runnable {
                tv_timer.setText(string)
                customHandler.postDelayed(this, 0)
            })

        }
    }

    fun stopRequest() {
        timerStatus = "On"
        btn_stop_request.setVisibility(View.GONE)
        btn_resume_request.setVisibility(View.VISIBLE)
        startTime = SystemClock.uptimeMillis()
        customHandler.postDelayed(updateTimerThread, 0)
        if (startDateAndTime == "")
            startDateAndTime = CommonMethods.getDateAndTime()
//        btn_submit.setVisibility(View.INVISIBLE)
        submitEditedJourney("stop", "")
    }

    fun resumeRequest() {
        timeSwapBuff += timeInMilliseconds
        timerData = tv_timer.text.toString()

        customHandler.removeCallbacks(updateTimerThread)
        timerStatus = "Off"
        btn_stop_request.setVisibility(View.VISIBLE)
        btn_resume_request.setVisibility(View.GONE)
        submitEditedJourney("resume", breakId)
    }

    fun changeDestination() {
        if (context is BaseActivity && timerStatus == "Off") {
            (context as BaseActivity).changeFragmentWithTag(ChangeDestinationFragment.newInstance(mParam1!!, mParam2!!, this), ChangeDestinationFragment.TAG)
            (context as DashboardActivity).setBackVisibility(true)
            (context as DashboardActivity).setToolbarTitle(getString(R.string.change_destination))
        } else {
            DialogUtil.showOkButtonDialog(context, "Please Resume Timer first.","","")
        }
    }

    private var address = ""
    private var lat = ""
    private var lng = ""

    private fun submitEditedJourney(time_type: String, breakId: String) {
        //if (context instanceof BaseActivity && timerStatus.equals("Off")) {
        CommonUtil.showProgressBar(rl_progress_bar)
        if (bundleData != null) {
            address = bundleData!!.getString("address", "")
            lat = bundleData!!.getString("newAddLat", "")
            lng = bundleData!!.getString("newAddLng", "")
        }
        BackgroundExecutor.instance.execute(EditJourneyRequester(EditJourneyRequest(mParam1, "break", CommonMethods.getDateAndTime(), time_type, address, lat, lng, breakId)))
        /* }
        else
        {
            DialogUtil.showOkButtonDialog(context,"Please Resume Timer first.");
        }*/
    }

    override fun sendUpdate(bundle: Bundle) {
        bundleData = bundle
        setDefaultTimer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        breakId = ""
        customHandler.removeCallbacks(updateTimerThread)
        timerStatus = "Off"
    }

}// Required empty public constructor
