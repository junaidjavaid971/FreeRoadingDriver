package com.app.freeroading.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.activities.BaseActivity
import com.apps.freeroadingdriver.activities.DashboardActivity
import com.apps.freeroadingdriver.adapter.TripHistoryAdapter
import com.apps.freeroadingdriver.constants.CommonMethods
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.fragments.BaseFragment
import com.apps.freeroadingdriver.fragments.TripHistoryDetailsFragment
import com.apps.freeroadingdriver.model.requestModel.TripHistoryRequest
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import com.apps.freeroadingdriver.model.responseModel.Road_trips
import com.apps.freeroadingdriver.requester.BackgroundExecutor
import com.apps.freeroadingdriver.requester.TripHistoryRequester
import com.apps.freeroadingdriver.utils.CommonUtil
import com.apps.freeroadingdriver.utils.DialogUtil
import com.apps.freeroadingdriver.utils.FragmentFactory
import kotlinx.android.synthetic.main.fragment_ride_history.*
import kotlinx.android.synthetic.main.progress_bar_layout.*
import org.greenrobot.eventbus.Subscribe
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER



class TripHistoryFragment : BaseFragment(), View.OnClickListener, TripHistoryAdapter.OnClickRow {
    override fun onRowClick(appointment_history: Road_trips?) {
        if (appointment_history!!.trip_status.equals("8")){
            DialogUtil.showOkButtonDialog(context,"Alert",getString(R.string.cancelled_ride),"")
        }else if( appointment_history!!.trip_status.equals("9")){
            DialogUtil.showOkButtonDialog(context,"Alert",getString(R.string.cancelled_ride_passenger),"")
        }else {
            FragmentFactory.addFragmentWithAnim(TripHistoryDetailsFragment.newInstance(appointment_history, param1!!), R.id.fragment_container, context, TAG)
            if (context is BaseActivity) {
                (context as DashboardActivity).setToolbarTitle(getString(R.string.ride_details))
            }
        }
    }


    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.st_calendar->{
                da.show()
                type_of_calendar = 1
            }
            R.id.end_calendar->{
                da.show()
                type_of_calendar = 2
            }
            R.id.go->{
                getHistoryFromServer()
            }
        }
    }

    private var param1: String? = null
    private var param2: String? = null
    private var rideHistoryAdapter: TripHistoryAdapter? = null
    private val rideHistories = ArrayList<Road_trips>()
    internal var sdf = SimpleDateFormat("yyyy-MM-dd")
    internal var cl = Calendar.getInstance()
    internal var cl1 = Calendar.getInstance()
    internal lateinit var da: DatePickerDialog
    internal var type_of_calendar = 0
    internal lateinit var strt: Date
    internal lateinit var endd: Date
    private var sDate: String? = null
    private var eDate:String? = null
    internal var mYear = cl.get(Calendar.YEAR)
    internal var mMonth = cl.get(Calendar.MONTH)
    internal var mDay = cl.get(Calendar.DAY_OF_MONTH)
    private val mDateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        mYear = year
        mMonth = monthOfYear + 1
        mDay = dayOfMonth
        updateDisplay(type_of_calendar)
    }


    private fun registerClickEvents(){
        st_calendar.setOnClickListener(this)
        end_calendar.setOnClickListener(this)
        go.setOnClickListener(this)
    }

    @Subscribe
   override fun onEvent(eventObject: EventObject) {
        if (eventObject == null) {
            return
        }
        requireActivity().runOnUiThread {
            CommonUtil.hideProgressBar(rl_progress_bar)
            onHandleBaseEvent(eventObject)
            val response = eventObject.`object` as BaseResponse
            when (eventObject.id) {
                EventConstant.GET_RIDE_HISTORY_SUCCESS -> {
                    rideHistories.clear()
                    rideHistories.addAll(response.response_data!!.road_trips!!)
                    if (rideHistoryAdapter == null) {
                        setAdapter()
                    } else {
                        rideHistoryAdapter?.notifyDataSetChanged()
                    }
                }
                EventConstant.GET_RIDE_HISTORY_ERROR -> {
                    rideHistories.clear()
                    rideHistoryAdapter?.notifyDataSetChanged()
                    if (response.response_msg.equals("Session not exist")) {
                        DialogUtil.showToastLongLength(context, response.response_msg)
                        (context as DashboardActivity).openLoginActivity()
                    } else {
                        DialogUtil.showToastLongLength(context, response.response_msg)
                    }
                }
            }
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trip_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        da = DatePickerDialog(requireActivity(), mDateSetListener,
                mYear, mMonth, mDay)
        sDate = sdf.format(cl.time)
        eDate = sdf.format(cl1.time)

        stdate.text = sDate
        enddate.text = eDate
        try {
            strt = sdf.parse(stdate.text.toString())
            endd = sdf.parse(enddate.text.toString())
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        registerClickEvents()
        getHistoryFromServer()
    }

    private fun getHistoryFromServer() {
        if (CommonMethods.isEmpty(stdate)) run { CommonMethods.showSnackBar(ride_parent, context, getString(R.string.start_date_validation)) }
        else if (CommonMethods.isEmpty(enddate)) run { CommonMethods.showSnackBar(ride_parent, context, getString(R.string.end_date_validation)) }
        else {
            CommonUtil.showProgressBar(rl_progress_bar)
            BackgroundExecutor.instance.execute(TripHistoryRequester(TripHistoryRequest(stdate.getText().toString(), enddate.getText().toString(),param1.toString(),param2.toString())))
        }
    }


    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                TripHistoryFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
    private fun updateDisplay(type: Int) {
        if (type == 1) {
            stdate.text = StringBuilder()
                    // Month is 0 based so add 1
                    .append(mYear).append("-")
                    .append(mMonth).append("-")
                    .append(mDay)
            try {

                strt = sdf.parse(stdate.text.toString())
            } catch (e: ParseException) {
                e.printStackTrace()
            }

        } else if (type == 2) {
            enddate.text = StringBuilder()
                    // Month is 0 based so add 1
                    .append(mYear).append("-")
                    .append(mMonth).append("-")
                    .append(mDay)
            try {
                endd = sdf.parse(enddate.text.toString())
            } catch (e: ParseException) {
                e.printStackTrace()
            }

        }
    }

    private fun setAdapter() {
        try {
            rideHistoryAdapter = TripHistoryAdapter(context, rideHistories, this)
            val mLayoutManager = LinearLayoutManager(context)
            rideHis.layoutManager = mLayoutManager
            rideHis.itemAnimator = DefaultItemAnimator()
            rideHis.adapter = rideHistoryAdapter
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
