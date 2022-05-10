package com.apps.freeroadingdriver.fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.activities.DashboardActivity
import com.apps.freeroadingdriver.adapter.ActiveRideListAdapter
import com.apps.freeroadingdriver.constants.AppConstant
import com.apps.freeroadingdriver.constants.CommonMethods
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.interfaces.SearchRoadRideClick
import com.apps.freeroadingdriver.model.requestModel.ActiveRideRequest
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import com.apps.freeroadingdriver.model.responseModel.Road_trips
import com.apps.freeroadingdriver.requester.ActiveRideListRequester
import com.apps.freeroadingdriver.requester.BackgroundExecutor
import com.apps.freeroadingdriver.utils.CommonUtil
import com.apps.freeroadingdriver.utils.DialogUtil
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_active_ride_list.*
import kotlinx.android.synthetic.main.progress_bar_layout.*
import org.greenrobot.eventbus.Subscribe
class ActiveRideListFragment : BaseFragment(), SearchRoadRideClick {
    override fun takeDatatoFragment(road_trips: Road_trips) {
        //  Toast.makeText(activity,"Coming soon", Toast.LENGTH_SHORT).show()
        (context as DashboardActivity).changeFragmentWithTag(ActiveRideDetailsFragment.newInstance(road_trips, ""), ActiveRideDetailsFragment.TAG)
        (context as DashboardActivity).setEnableDisableDrawer(flag = false)
        (context as DashboardActivity).setBackVisibility(true)
        (context as DashboardActivity).setToolbarTitle(getString(R.string.active_ride_detail))
    }
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private lateinit var list_road: ArrayList<Road_trips>
    private lateinit var activeRideListAdapter: ActiveRideListAdapter

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
        return inflater.inflate(R.layout.fragment_active_ride_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list_road = ArrayList()
        CommonUtil.showProgressBar(rl_progress_bar)
        BackgroundExecutor.instance.execute(ActiveRideListRequester(ActiveRideRequest(CommonMethods.getCurrentDateOnly(), AppConstant.USER_TYPE)))
    }

    companion object {
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        fun newInstance(param1: String, param2: String): ActiveRideListFragment {
            val fragment = ActiveRideListFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onResume() {
        super.onResume()
        (context as DashboardActivity).iv_details.visibility = View.GONE
    }

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        if (eventObject == null) {
            return
        }
        requireActivity().runOnUiThread(Runnable {
            try {
                CommonUtil.hideProgressBar(rl_progress_bar)
        onHandleBaseEvent(eventObject)
        val response = eventObject.`object` as BaseResponse
        when (eventObject.id) {
            EventConstant.ACTIVELIST_SUCCESS -> {
                if (response.response_data?.road_trips != null && response.response_data?.road_trips?.size!! > 0) {
                    list_road.addAll(response.response_data!!.road_trips!!)
                }
                setAdapter()
            }
            EventConstant.ACTIVELIST_ERROR -> {
                DialogUtil.showOkButtonDialog(context, getString(R.string.message), response.response_msg, response.response_key)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
})
    }

    private fun setAdapter() {
        activeRideListAdapter = ActiveRideListAdapter(context, list_road, this)
        val mLayoutManager = LinearLayoutManager(context)
        activeView.setLayoutManager(mLayoutManager)
        activeView.setItemAnimator(DefaultItemAnimator())
        activeView.setAdapter(activeRideListAdapter)

    }
}// Required empty public constructor
