package com.apps.freeroadingdriver.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apps.freeroadingdriver.FreeRoadingApp

import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.activities.DashboardActivity
import com.apps.freeroadingdriver.constants.CommonMethods
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.model.requestModel.StatisticsRequest
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import com.apps.freeroadingdriver.requester.BackgroundExecutor
import com.apps.freeroadingdriver.requester.StatisticsRequester
import com.apps.freeroadingdriver.utils.CommonUtil
import com.apps.freeroadingdriver.utils.DialogUtil
import kotlinx.android.synthetic.main.fragment_statistics.*
import kotlinx.android.synthetic.main.progress_bar_layout.*
import org.greenrobot.eventbus.Subscribe


/**
 * A simple [Fragment] subclass.
 * Use the [StatisticsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StatisticsFragment : BaseFragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

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
        val view = inflater!!.inflate(R.layout.fragment_statistics, container, false)
        hitStatisticsApi()
        return view
    }

    fun hitStatisticsApi(){
        CommonUtil.showProgressBar(rl_progress_bar)
        BackgroundExecutor.instance.execute(StatisticsRequester(StatisticsRequest(CommonMethods.getCurrentDateString(),CommonMethods.getTimeZone())))
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        private val TAG = StatisticsFragment::class.java.name
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StatisticsFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): StatisticsFragment {
            val fragment = StatisticsFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    @Subscribe override fun onEvent(eventObject: EventObject) {
        Log.d(TAG,"OnEvent called")
        if (eventObject == null){
            return
        }
        requireActivity().runOnUiThread(Runnable {
            try {
                CommonUtil.hideProgressBar(rl_progress_bar)
                onHandleBaseEvent(eventObject)
                val response = eventObject.`object` as BaseResponse
                when (eventObject.id) {
                    EventConstant.STATISTICS_SUCCESS->{
                        val res = response.response_data!!.statistics
                        totalearn.setText("$"+res?.total_earning)
                        monthearning.setText("$"+res?.total_month_earning)
                        weekearn.setText("$"+res?.total_week_earning)
                        todayearn.setText("$"+res?.total_todays_earning)
                        last_fare.setText("$"+res?.last_bill)
                        total_booking.setText(res?.total_booking)
                    }
                    EventConstant.STATISTICS_ERROR->{
                        Log.d(DashboardActivity.TAG, "statistics error")
                        DialogUtil.showOkButtonDialog(context, getString(R.string.message), response.response_msg, response.response_key)
                    }
                }
            }catch (e : Exception){
                e.printStackTrace()
            }
        })
    }
}// Required empty public constructor
