package com.apps.freeroadingdriver.fragments


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
import com.apps.freeroadingdriver.adapter.RideHistoryAdapter
import com.apps.freeroadingdriver.constants.CommonMethods
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.model.requestModel.RideHistoryRequest
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import com.apps.freeroadingdriver.model.responseModel.Home_data
import com.apps.freeroadingdriver.requester.BackgroundExecutor
import com.apps.freeroadingdriver.requester.RideHistoryRequester
import com.apps.freeroadingdriver.utils.CommonUtil
import com.apps.freeroadingdriver.utils.DialogUtil
import com.apps.freeroadingdriver.utils.FragmentFactory
import kotlinx.android.synthetic.main.fragment_ride_history.*
import kotlinx.android.synthetic.main.progress_bar_layout.*
import org.greenrobot.eventbus.Subscribe
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [RideHistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RideHistoryFragment : BaseFragment(), RideHistoryAdapter.OnClickRow,View.OnClickListener {
    override fun onRowClick(appointment_history: Home_data) {
        if (appointment_history.status.equals("8")){
         DialogUtil.showOkButtonDialog(requireActivity(),"Alert",getString(R.string.cancelled_ride),"")
        }else if( appointment_history.status.equals("9")){
            DialogUtil.showOkButtonDialog(requireActivity(),"Alert",getString(R.string.cancelled_ride_passenger),"")
        }
        else {
            FragmentFactory.addFragmentWithAnim(RideHIstoryDetailFragment.newInstance(appointment_history, ""), R.id.fragment_container, requireActivity(), TAG)
            if (context is BaseActivity) {
                (context as DashboardActivity).setToolbarTitle(getString(R.string.ride_details))
            }
        }
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
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

    private fun registerClickEvents(){
        st_calendar.setOnClickListener(this)
        end_calendar.setOnClickListener(this)
        go.setOnClickListener(this)
    }
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var rideHistoryAdapter: RideHistoryAdapter? = null
    private val rideHistories = ArrayList<Home_data>()
    internal var sdf = SimpleDateFormat("yyyy-MM-dd")
    internal var cl = Calendar.getInstance()
    internal var cl1 = Calendar.getInstance()
    internal lateinit var da: DatePickerDialog
    internal var type_of_calendar = 0
    internal lateinit var strt: Date
    internal lateinit var endd:Date
    private var sDate: String? = null
    private var eDate:String? = null
    internal var mYear = cl.get(Calendar.YEAR)
    internal var mMonth = cl.get(Calendar.MONTH)
    internal var mDay = cl.get(Calendar.DAY_OF_MONTH)
    private val isFragmentLoaded = false
    private val mDateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        mYear = year
        mMonth = monthOfYear + 1
        mDay = dayOfMonth
        updateDisplay(type_of_calendar)
    }

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
        return inflater!!.inflate(R.layout.fragment_ride_history, container, false)
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
         * @return A new instance of fragment RideHistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): RideHistoryFragment {
            val fragment = RideHistoryFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
    private fun getHistoryFromServer() {
        if (CommonMethods.isEmpty(stdate)) run { CommonMethods.showSnackBar(ride_parent, requireActivity(), getString(R.string.start_date_validation)) }
        else if (CommonMethods.isEmpty(enddate)) run { CommonMethods.showSnackBar(ride_parent, requireActivity(), getString(R.string.end_date_validation)) }
        else {
            CommonUtil.showProgressBar(rl_progress_bar)
            BackgroundExecutor.instance.execute(RideHistoryRequester(RideHistoryRequest(stdate.getText().toString(), enddate.getText().toString())))
        }
    }
    @Subscribe override fun onEvent(eventObject: EventObject) {
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
                    rideHistories.addAll(response.response_data!!.appointment!!)
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
                        DialogUtil.showToastLongLength(requireActivity(), response.response_msg)
                        (context as DashboardActivity).openLoginActivity()
                    } else {
                        DialogUtil.showToastLongLength(requireActivity(), response.response_msg)
                    }
                }
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
            rideHistoryAdapter = RideHistoryAdapter(requireActivity(), rideHistories, this)
            val mLayoutManager = LinearLayoutManager(context)
            rideHis.layoutManager = mLayoutManager
            rideHis.itemAnimator = DefaultItemAnimator()
            rideHis.adapter = rideHistoryAdapter
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}// Required empty public constructor
