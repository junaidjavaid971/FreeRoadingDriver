package com.apps.freeroadingdriver.fragments


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.activities.DashboardActivity
import com.apps.freeroadingdriver.constants.CommonMethods
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.model.requestModel.EditJourneyRequest
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import com.apps.freeroadingdriver.requester.BackgroundExecutor
import com.apps.freeroadingdriver.requester.EditJourneyRequester
import com.apps.freeroadingdriver.utils.CommonUtil
import com.apps.freeroadingdriver.utils.DialogUtil
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import kotlinx.android.synthetic.main.fragment_change_destination.*
import kotlinx.android.synthetic.main.progress_bar_layout.*
import org.greenrobot.eventbus.Subscribe


/**
 * A simple [Fragment] subclass.
 * Use the [ChangeDestinationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChangeDestinationFragment : BaseFragment() {

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        if (eventObject == null) {
            return
        }
        requireActivity().runOnUiThread {
            try {
                CommonUtil.hideProgressBar(rl_progress_bar)
                onHandleBaseEvent(eventObject)
                val response = eventObject.`object` as BaseResponse
                when (eventObject.id) {
                    EventConstant.EDIT_JOURNEY_SUCCESS -> {
//                        (context as BaseActivity).onBackPressed()
                        startActivity(DashboardActivity.createIntent(requireActivity()))
                        requireActivity().finish()
                    }
                    EventConstant.EDIT_JOURNEY_ERROR -> {
                        DialogUtil.showOkButtonDialog(
                            context,
                            "Alert",
                            response.response_msg,
                            response.response_key
                        )
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_destination, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDefaultView()
        submitBtn.setOnClickListener(View.OnClickListener {
            saveNewLocation()
        })

        rl_drop_off_location.setOnClickListener(View.OnClickListener {
            changeDestination()
        })
    }

    private fun setDefaultView() {
        dropoffPoint.setText(mParam2)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        lateinit var journeyResponse: EditJourneyResponse
        val TAG = ChangeDestinationFragment::class.java.name

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChangeDestinationFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(
            param1: String,
            param2: String,
            journeyResponseValue: EditJourneyResponse
        ): ChangeDestinationFragment {
            val fragment = ChangeDestinationFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            journeyResponse = journeyResponseValue
            return fragment
        }
    }

    fun changeDestination() {
        placeSearchRequest(1001)
    }

    private fun placeSearchRequest(requestCode: Int) {
        var intent: Intent? = null
        try {
            intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                .build(context as DashboardActivity)
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }

        startActivityForResult(intent, requestCode)
    }

    lateinit var newAddLat: String
    lateinit var newAddLng: String
    private var bundleData: Bundle? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var addrs: String? = null
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            val place = PlaceAutocomplete.getPlace(context as DashboardActivity, data)
            Log.e("Place", place.toString())
            addrs = place.address.toString()
            val name = place.name.toString()
            Log.e("Address", "$name, $addrs")
            newdropOffPoint.setText(addrs)
            val latLng = place.latLng
            newAddLat = latLng.latitude.toString()
            newAddLng = latLng.longitude.toString()
            bundleData = Bundle()
            bundleData!!.putString("address", addrs)
            bundleData!!.putString("newAddLat", newAddLat)
            bundleData!!.putString("newAddLng", newAddLng)
        }
    }

    fun saveNewLocation() {
        if (bundleData != null) {
            CommonUtil.showProgressBar(rl_progress_bar)
            BackgroundExecutor.instance.execute(
                EditJourneyRequester(
                    EditJourneyRequest(
                        mParam1,
                        "location",
                        CommonMethods.getDateAndTime(),
                        "",
                        bundleData!!.getString("address", ""),
                        bundleData!!.getString("newAddLat", ""),
                        bundleData!!.getString("newAddLng", ""),
                        ""
                    )
                )
            )
            //journeyResponse.sendUpdate(bundleData);
        } else
            DialogUtil.showOkButtonDialog(context, "Something went wrong!!", "", "")
    }

    interface EditJourneyResponse {
        fun sendUpdate(bundleData: Bundle)
    }

}// Required empty public constructor
