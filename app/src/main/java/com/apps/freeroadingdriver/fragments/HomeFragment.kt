package com.apps.freeroadingdriver.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.activities.BaseActivity
import com.apps.freeroadingdriver.activities.DashboardActivity
import com.apps.freeroadingdriver.adapter.StatusAdapter
import com.apps.freeroadingdriver.constants.AppConstant
import com.apps.freeroadingdriver.constants.CommonMethods
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.manager.LocationManagerWIthGps
import com.apps.freeroadingdriver.manager.UserManager
import com.apps.freeroadingdriver.model.requestModel.*
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import com.apps.freeroadingdriver.model.responseModel.Home_data
import com.apps.freeroadingdriver.model.responseModel.ResponseData
import com.apps.freeroadingdriver.model.responseModel.Status
import com.apps.freeroadingdriver.network.HTTPOperationController
import com.apps.freeroadingdriver.network.URLConstant
import com.apps.freeroadingdriver.permissions.PermissionsUtil
import com.apps.freeroadingdriver.prefrences.BasePreferences
import com.apps.freeroadingdriver.prefrences.FreeRoadingPreferenceManager
import com.apps.freeroadingdriver.pubnub.PubNubManager
import com.apps.freeroadingdriver.push.MessagingService
import com.apps.freeroadingdriver.requester.*
import com.apps.freeroadingdriver.utils.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.on_the_way.*
import kotlinx.android.synthetic.main.progress_bar_layout.*
import kotlinx.android.synthetic.main.request_accept_reject_layout.*
import org.greenrobot.eventbus.Subscribe
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class HomeFragment : BaseFragment(), View.OnClickListener, OnMapReadyCallback,
    StatusAdapter.StatusListener {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var googleMap: GoogleMap? = null
    internal lateinit var locationManager: LocationManagerWIthGps
    internal lateinit var gpsTracker: GPSTracker
    private var mLocationManager: LocationManager? = null
    private val MAKE_CALL_PERMISSION_REQUEST_CODE = 1
    private var lattitude: Double = 0.toDouble()
    private var longitude: Double = 0.toDouble()
    internal var bearing = 0.0f
    private var bookingId = ""
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
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        if (checkPermission(Manifest.permission.CALL_PHONE)) {
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CALL_PHONE),
                MAKE_CALL_PERMISSION_REQUEST_CODE
            )
        }
        return view
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationManager = LocationManagerWIthGps.getInstance()
        mLocationManager = requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager
        gpsTracker = GPSTracker(activity)
        lattitude = gpsTracker.location.latitude
        longitude = gpsTracker.location.longitude
        mCurrentLat = "" + locationManager.getLatitude()
        mCurrentLng = "" + locationManager.getLongitude()
        //Getting today's date
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        registerClickEvents()
        getUserRideStatus()
        updateDriverLocationOnMap()
        updateStripeStatus()
        Log.e("device Token", FreeRoadingPreferenceManager.getInstance().deviceToken)
    }

    override fun onMapReady(p0: GoogleMap) {
        this.googleMap = p0
        val lattitude: Double
        val longitude: Double
        Log.d("lattitude", "" + locationManager.latitude)
        if (locationManager.latitude == 0.0) {
            lattitude = gpsTracker.location.latitude
            longitude = gpsTracker.location.longitude
        } else {
            lattitude = locationManager.latitude
            longitude = locationManager.longitude
        }
        initMap(lattitude, longitude)
        initBroadCastReciever()
    }

    private fun initMap(lattitude: Double, longitude: Double) {
        val position = LatLng(lattitude, longitude)
        //MarkerOptions options = new MarkerOptions();
        //options.position(position);
        // Setting position for the MarkerOptions
        // Gets to GoogleMap from the MapView and does initialization stuff
        val icon: BitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.car)
        val markerOptions: MarkerOptions = MarkerOptions().position(position);
        markerOptions.icon(icon)
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        googleMap?.addMarker(markerOptions);
        googleMap?.setMapType(GoogleMap.MAP_TYPE_TERRAIN)
        googleMap?.getUiSettings()?.isCompassEnabled = false
        googleMap?.getUiSettings()?.isMyLocationButtonEnabled = true
        googleMap?.setMyLocationEnabled(true)

        // Needs to call MapsInitializer before doing any CameraUpdateFactory
        // calls
        MapsInitializer.initialize(requireActivity())
        //google_map.addMarker(options);
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(position, AppConstant.MAP_ZOOM))
        // Zoom in, animating the camera.
        googleMap?.animateCamera(CameraUpdateFactory.zoomIn())
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap?.animateCamera(CameraUpdateFactory.zoomTo(AppConstant.MAP_ZOOM), 100, null)

        googleMap?.setOnCameraChangeListener(GoogleMap.OnCameraChangeListener { arg0 ->
            // google_map.clear();
            val perth = googleMap?.addMarker(MarkerOptions().position(arg0.target).draggable(true))
            perth?.isVisible = false
        })
    }

    private fun getUserRideStatus() {
        CommonUtil.showProgressBar(rl_progress_bar)
        BackgroundExecutor.instance.execute(
            UserRideStatusRequester(
                BaseRequest(
                    false,
                    true,
                    false,
                    false
                )
            )
        )
    }

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        Log.d(TAG, "onEvent method called")
        if (eventObject == null) {
            return
        }
        requireActivity().runOnUiThread {
            try {
                CommonUtil.hideProgressBar(rl_progress_bar)
                onHandleBaseEvent(eventObject)
                var response = eventObject.`object` as BaseResponse
                when (eventObject.id) {
                    EventConstant.RIDE_ACCEPT_SUCCESS -> {
                        if (timer != null)
                            timer!!.cancel()
                        countDownTimer!!.onFinish()
                        countDownTimer!!.cancel()
                        hideAndShowLayout("driver_on_the_way")
//                        bookingId = response.response_data!!.home_data!!.app_appointment_id!!
                        bookingId = response.response_data!!.app_appointment_id!!
                        publishDataOnPubnubWithIntervals(
                            "5",
                            "freeroading_" + response.response_data!!.passenger_device_id
                        )
                        updateMapAfterRecieverStatus(3)
                        FreeRoadingPreferenceManager.getInstance().setRideStatus("On")
                        FreeRoadingPreferenceManager.getInstance().setDistance(0.0)
                    }
                    EventConstant.RIDE_ACCEPT_ERROR, EventConstant.RIDE_REJECT_ERROR, EventConstant.GET_RIDE_HISTORY_DETAIL_ERROR
                    -> if (response.response_msg.equals("Session not exist")) {
                        DialogUtil.showToastLongLength(context, response.response_msg)
                        (context as DashboardActivity).openLoginActivity()
                    } else {
                        DialogUtil.showToastLongLength(context, response.response_msg)
                        rideRejectedSuccefully()
                    }
                    EventConstant.GET_RIDE_HISTORY_DETAIL_SUCCESS -> {
                        displayBookingResponse(response.response_data!!)
                        Handler().postDelayed(
                            Runnable { MessagingService.cancelNotification(context) },
                            2000
                        )
                    }

                    EventConstant.RIDE_REJECT_SUCCESS -> {
                        updateIOSPassenegr("1")
                        rideRejectedSuccefully()
                    }
                    EventConstant.I_HAVE_ARRIVED_SUCCESS -> iHaveArrivedSuccefully("Success")
                    EventConstant.I_HAVE_ARRIVED_ERROR -> if (response.response_msg.equals("Session not exist")) {
                        DialogUtil.showToastLongLength(context, response.response_msg)
                        (context as DashboardActivity).openLoginActivity()
                    } else {
                        DialogUtil.showToastLongLength(context, response.response_msg)
                        iHaveArrivedSuccefully("Error")
                    }
                    EventConstant.BEGIN_JOURNEY_SUCCESS -> beginJourneySuccessfully("Success")
                    EventConstant.BEGIN_JOURNEY_ERROR -> if (response.response_msg.equals("Session not exist")) {
                        DialogUtil.showToastLongLength(context, response.response_msg)
                        (context as DashboardActivity).openLoginActivity()
                    } else {
                        DialogUtil.showToastLongLength(context, response.response_msg)
                        beginJourneySuccessfully("Error")
                    }
                    EventConstant.DROPPED_PASSENGER_SUCCESS -> passengerDroppedSuccefully(response.response_data!!)
                    EventConstant.DROPPED_PASSENGER_ERROR -> if (response.response_msg.equals("Session not exist")) {
                        DialogUtil.showToastLongLength(context, response.response_msg)
                        (context as DashboardActivity).openLoginActivity()

                        DialogUtil.showToastLongLength(context, response.response_msg)
                    } else {
                        DialogUtil.showToastLongLength(context, response.response_msg)
                    }
                    EventConstant.GET_HOME_DATA_SUCCESS -> {
                        getRideStatusResponse(response.response_data!!.home_data!!)
                        if (response.response_data!!.home_data!!.getStatus()
                                .equals("5") || response.response_data!!.home_data!!.getStatus()
                                .equals("4") || response.response_data!!.home_data!!.getStatus()
                                .equals("3") || response.response_data!!.home_data!!.getStatus()
                                .equals("2")
                        ) {
                            FreeRoadingPreferenceManager.getInstance().rideActive = true
                        } else FreeRoadingPreferenceManager.getInstance().rideActive = false
                    }
                    EventConstant.GET_HOME_DATA_ERROR -> if (response.response_msg.equals("Session not exist")) {
                        DialogUtil.showToastLongLength(context, response.response_msg)
                        (context as DashboardActivity).openLoginActivity()
                    } else if (response.response_msg.equals("Processed") || response.response_key.equals(
                            "24"
                        )
                    ) {
                        setStatusAdapter(response.response_data!!.check_status!!)
                    } else {
                        DialogUtil.showToastLongLength(context, response.response_msg)
                    }


                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun updateStripeStatus() {
        /* if (FreeRoadingPreferenceManager.getInstance().stripeToken == null || FreeRoadingPreferenceManager.getInstance().stripeToken.equals("0")) {
             stripe_message_tv.setVisibility(View.VISIBLE)
             stripe_message_tv.setText(Html.fromHtml("In order to receive payment <br/><b><u><font text-color=\'#151515\'>Connect with Stripe</font></u></b>"))
             stripe_message_tv.setMovementMethod(LinkMovementMethod.getInstance())
         } else {
             stripe_message_tv.setVisibility(View.GONE)
         }*/
    }


    private fun displayBookingResponse(rideHistory: ResponseData) {
        if (mRideRequest) {
            hideAndShowLayout("ride_request")
            showCountDownTimer()
            mRideRequest = false
        }
        tv_ride_request_booking_id.setText("Booking Id : $bookingId")
        tv_pick_up_address.setText(rideHistory.pick_address)
        mCustomerLat = rideHistory.pick_latitude
        mCustomerLng = rideHistory.pick_longitude
        mDropOffLng = rideHistory.drop_longitude
        mDropOffLat = rideHistory.drop_latitude
        mPickAddress = rideHistory.pick_address
        mDropAddress = rideHistory.drop_address
        mobileNo = rideHistory.mobile!!
        GlideUtil.loadImageWithDefaultImage(
            context as BaseActivity,
            iv_profile,
            URLConstant.PASSENGER_IMAGE_URL + rideHistory.profile_pic,
            R.drawable.ic_profile_placeholder
        )
        setReachableTimeAndDistance()
        tv_journy_started_bookingid.setText("Booking Id : $bookingId")
        tv_pick_up_location.setText(rideHistory.pick_address)
        tv_drop_off_location.setText(rideHistory.drop_address)
        tv_driver_name.setText(rideHistory.customer_name)
        location.setText(rideHistory.pick_address)
    }

    private fun showCountDownTimer() {
        updateCountDownTimer()
    }

    internal var countDownTimer: CountDownTimer? = null
    private fun updateCountDownTimer() {
        countDownTimer = object : CountDownTimer((30 * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                txt_timer?.setText(hmsTimeFormatter(millisUntilFinished))
            }

            override fun onFinish() {
                rideRejectedSuccefully()
            }
        }
        countDownTimer!!.start()
    }

    private fun hmsTimeFormatter(milliSeconds: Long): String {
        return String.format(
            "%02d",
            TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(
                    milliSeconds
                )
            )
        )
    }


    private fun setReachableTimeAndDistance() {
        try {
            val distancePath =
                "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=$mCustomerLat,$mCustomerLng+&destinations=$mDropOffLat,$mDropOffLng"
            val downloadTask = ReadTask("distance", "")
            downloadTask.execute(distancePath)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        if (context is DashboardActivity)
            (context as DashboardActivity).setBackVisibility(false)
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        PermissionsUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
        when (requestCode) {
            MAKE_CALL_PERMISSION_REQUEST_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                return
            }
        }
    }

    var timer: Timer? = null
    private val TIMER_WAIT_TIME = 5000
    private var mCurrentLat: String? = null
    private var mCurrentLng: String? = null
    private var passengerChannal = ""
    private fun publishDataOnPubnubWithIntervals(statusCode: String, channalName: String) {
        passengerChannal = channalName
        if (timer != null) {
            timer!!.cancel()
        }
        timer = Timer()
        val timerTask = object : TimerTask() {
            override fun run() {
                try {
                    PubNubManager.getPubnubManager().publish(getPostData(statusCode), channalName)
                    if (bookingId == "") {
                        (context as? BaseActivity)?.runOnUiThread(Runnable {
                            drawMarker(
                                LatLng(
                                    gpsTracker.location.getLatitude(),
                                    gpsTracker.location.getLongitude()
                                )
                            )
                        })
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
        timer!!.scheduleAtFixedRate(timerTask, 3000, TIMER_WAIT_TIME.toLong())

    }

    private var markerMapOnTheWay: Marker? = null
    private fun drawMarker(point: LatLng) {
        (context as? BaseActivity)?.runOnUiThread(Runnable {
            clearMap()
            val markerOptions = MarkerOptions()
            markerOptions.position(point)
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ci_car_available))
            markerMapOnTheWay = googleMap!!.addMarker(markerOptions)
            googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(point, AppConstant.MAP_ZOOM))
        })
    }

    private fun clearMap() {
        googleMap!!.clear()
        googleMap!!.setPadding(0, 600, 0, 0)
    }

    private fun getPostData(statusCode: String): JsonObject {
        val mObject = JsonObject()
        try {
            when (statusCode) {
                "2" -> {
                    mObject.addProperty("rq_tp", "1")
                    mObject.addProperty("id", UserManager.getInstance().getUser().driver_id)
                    mObject.addProperty("lt", gpsTracker.location.getLatitude())
                    mObject.addProperty("lg", gpsTracker.location.getLongitude())
                    mObject.addProperty(
                        "android_driver_name",
                        UserManager.getInstance().getUser().name
                    )
                    mObject.addProperty(
                        "chn",
                        FreeRoadingPreferenceManager.getInstance().pubNubChan
                    )
                }
                "5" -> {
                    mObject.addProperty("rq_tp", "5")
                    mObject.addProperty("id", UserManager.getInstance().getUser().driver_id)
                    mObject.addProperty("lt", gpsTracker.location.getLatitude())
                    mObject.addProperty("lg", gpsTracker.location.getLongitude())
                    mObject.addProperty("email", UserManager.getInstance().getUser().email)
                    mObject.addProperty("mobile", UserManager.getInstance().getUser().mobile)
                    mObject.addProperty("name", UserManager.getInstance().getUser().name)
                    mObject.addProperty(
                        "appointment_id",
                        FreeRoadingPreferenceManager.getInstance().appointmentId
                    )
                    mObject.addProperty(
                        "chn",
                        FreeRoadingPreferenceManager.getInstance().pubNubChan
                    )
                }
                "" -> {
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        Log.d("$TAG Publish ", mObject.toString())
        return mObject
    }

    //for testing...
    internal fun updateDriverLocationOnMap() {
        PubNubManager.getPubnubManager()
            .subscribe(Arrays.asList("freeroading"), object : SubscribeCallback() {
                override fun status(pubnub: PubNub, status: PNStatus) {}

                override fun message(pubnub: PubNub, message: PNMessageResult) {
                    (context as? BaseActivity)?.runOnUiThread {
                        try {
                            val updates =
                                Gson().fromJson(message.message, DriverUpdates::class.java)
                            animateMarkerNew(
                                LatLng(
                                    java.lang.Double.parseDouble(updates.lt),
                                    java.lang.Double.parseDouble(updates.lg)
                                ), markerMapOnTheWay, "journeystarted"
                            )
                        } catch (e: JsonSyntaxException) {
                            e.printStackTrace()
                        } catch (e: NumberFormatException) {
                            e.printStackTrace()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun presence(pubnub: PubNub, presence: PNPresenceEventResult) {}
            })
    }

    internal inner class DriverUpdates {
        var lt: String? = null
        var lg: String? = null

        var rq_tp: String? = null
    }

    private fun animateMarkerNew(destination: LatLng, marker: Marker?, rideStatus: String) {

        if (marker != null) {

            val startPosition = marker.position
            marker.setPosition(destination)

            // save distance when journey started.
            if (rideStatus != "ontheway") {
                try {
                    val distanceInMeters = LocationUtil.getDistanceInMeters(
                        LocationUtil.distanceBetweenTwoPoint(
                            startPosition.latitude,
                            startPosition.longitude,
                            destination.latitude,
                            destination.longitude
                        )
                    )
                    if (FreeRoadingPreferenceManager.getInstance().getDistance() !== 0.0) {
                        FreeRoadingPreferenceManager.getInstance().setDistance(
                            distanceInMeters + FreeRoadingPreferenceManager.getInstance()
                                .getDistance()
                        )
                    } else {
                        FreeRoadingPreferenceManager.getInstance().setDistance(distanceInMeters)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            lattitude = location.latitude
            longitude = location.longitude
            bearing = location.bearing
        }

        override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {

        }

        override fun onProviderEnabled(s: String) {

        }

        override fun onProviderDisabled(s: String) {

        }
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        private val TAG = HomeFragment::class.java.name

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    private fun registerClickEvents() {
        btn_accept_request.setOnClickListener(this)
        btn_reject_request.setOnClickListener(this)
        btn_arrived_request.setOnClickListener(this)
        btn_call_driver.setOnClickListener(this)
        btn_begin_journy.setOnClickListener(this)
        btn_edt_journy.setOnClickListener(this)
        btn_drop_journy.setOnClickListener(this)
        btn_dropedit_journy.setOnClickListener(this)
        btn_cancel_request.setOnClickListener(this)
        stripe_message_tv.setOnClickListener(this)
    }


    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btn_accept_request -> {
                startDriverOnTheWayPage()
            }
            R.id.btn_reject_request -> {
                CommonUtil.showProgressBar(rl_progress_bar)
                BackgroundExecutor.instance.execute(
                    RejectRideRequester(
                        RejectRideRequest(
                            bookingId,
                            CommonMethods.getCurrentDateString(),
                            CommonMethods.getTimeZone()
                        )
                    )
                )

            }
            R.id.btn_arrived_request -> {
                CommonUtil.showProgressBar(rl_progress_bar)
                BackgroundExecutor.instance.execute(
                    DriverArrivedRequester(
                        DriverArrivedRequest(
                            bookingId,
                            CommonMethods.getCurrentDateString(),
                            CommonMethods.getTimeZone(),
                            "4"
                        )
                    )
                )
            }
            R.id.btn_call_driver -> {
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:$mobileNo")

                if (ActivityCompat.checkSelfPermission(
                        context as BaseActivity,
                        Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                startActivity(callIntent)
            }
            R.id.btn_begin_journy -> {
                mCustomerLat = "" + gpsTracker.location.getLatitude()
                mCustomerLng = "" + gpsTracker.location.getLongitude()
                CommonUtil.showProgressBar(rl_progress_bar)
                BackgroundExecutor.instance.execute(
                    BeginJourneyRequester(
                        BeginJourneyRequest(
                            bookingId,
                            CommonMethods.getCurrentDateString(),
                            CommonMethods.getTimeZone(),
                            "3",
                            mCustomerLat,
                            mCustomerLng,
                            mPickAddress
                        )
                    )
                )

            }
            R.id.btn_cancel_request -> {
                FragmentFactory.replaceFragmentWithAnim(
                    CancelRequestFragment.newInstance(
                        bookingId,
                        FreeRoadingPreferenceManager.getInstance().pubNubChan
                    ), R.id.fragment_container, context, TAG
                )
                (context as? DashboardActivity)?.setBackVisibility(true)
                (context as? DashboardActivity)?.setToolbarTitle(getString(R.string.cancel))
            }
            R.id.btn_drop_journy -> {
                DialogUtil.showTwoButtonDialog(
                    activity,
                    getString(R.string.title_exit),
                    getString(R.string.msg_drop_journey),
                    getString(R.string.txt_yes),
                    getString(R.string.txt_no),
                    object : DialogUtil.AlertDialogInterface.TwoButtonDialogClickListener {
                        override fun onPositiveButtonClick() {
                            CommonUtil.showProgressBar(rl_progress_bar)
                            val distanceInMiles = "" + (LocationUtil.getMilesFrmMeter(
                                FreeRoadingPreferenceManager.getInstance().getDistance()
                            ))
                            BackgroundExecutor.instance.execute(
                                DroppedPassengerRequester(
                                    DroppedPassengerRequest(
                                        bookingId,
                                        CommonMethods.getCurrentDateString(),
                                        CommonMethods.getTimeZone(),
                                        "2",
                                        "" + gpsTracker.location.getLatitude(),
                                        "" + gpsTracker.location.getLongitude(),
                                        LocationUtil.getAddressFromLatLong(
                                            gpsTracker.location.getLatitude(),
                                            gpsTracker.location.getLongitude()
                                        ),
                                        distanceInMiles
                                    )
                                )
                            )
                        }

                        override fun onNegativeButtonClick() {}
                    })
            }
            R.id.btn_edt_journy, R.id.btn_dropedit_journy -> {
                (context as BaseActivity).changeFragmentWithTag(
                    EditJourneyFragment.newInstance(
                        bookingId,
                        mDropAddress!!
                    ), EditJourneyFragment.TAG
                )
                (context as DashboardActivity).setBackVisibility(true)
                (context as DashboardActivity).setToolbarTitle(getString(R.string.edit_journey))
            }
            R.id.stripe_message_tv -> {
                (context as BaseActivity)?.changeFragmentWithTag(
                    StripeFragment.newInstance("", ""),
                    StripeFragment.TAG
                )
                (context as DashboardActivity)?.setBackVisibility(true)
                (context as DashboardActivity)?.setToolbarTitle(getString(R.string.stripe_connect))
            }
        }
    }

    private fun startRideRequestPage() {
        initRideDetailsOnRequestPage()
    }

    private fun initRideDetailsOnRequestPage() {
        CommonUtil.showProgressBar(rl_progress_bar)
        BackgroundExecutor.instance.execute(
            GetRideHistoryDetailRequester(
                RideHistoryDetailRequest(
                    bookingId
                )
            )
        )
    }


    private fun startDriverOnTheWayPage() {
        CommonUtil.showProgressBar(rl_progress_bar)
        BackgroundExecutor.instance.execute(
            AcceptRideRequester(
                AcceptRideRequest(
                    bookingId,
                    CommonMethods.getCurrentDateString(),
                    CommonMethods.getTimeZone()
                )
            )
        )
    }


    override fun onDestroy() {
        super.onDestroy()

    }

    // register braoadcast reciever to recieve action event.
    private fun registerBroadCastReciever() {
        val onRideRequestIntent = IntentFilter()
        onRideRequestIntent.addAction(AppConstant.RIDE_REQUEST_COMES)
        requireActivity().registerReceiver(onRideRequestReciever, onRideRequestIntent)
    }

    private var onRideRequestReciever: BroadcastReceiver? = null
    private var mRideRequest = false
    private fun initBroadCastReciever() {
        onRideRequestReciever = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                bookingId = intent.getStringExtra("appointment_id").toString()
                FreeRoadingPreferenceManager.getInstance().appointmentId = bookingId
                if (mGeofenceTimer != null) mGeofenceTimer!!.cancel()

                if (intent.getStringExtra("type").equals("road-trip")) {
                    Log.d(TAG, "Road truo request")
                } else {
                    if (intent.getStringExtra("response") == "6") {
                        startRideRequestPage()
                        mRideRequest = true
                        FreeRoadingPreferenceManager.getInstance().setEstimatedTime("0")
                    } else if (intent.getStringExtra("response") == "9") {
                        CommonMethods.showAlert(
                            context,
                            "Alert !",
                            intent.getStringExtra("message")
                        )
                        rideRejectedSuccefully()
                        FreeRoadingPreferenceManager.getInstance().setEstimatedTime("0")
                    } else if (intent.getStringExtra("response") == "1") {
                        CommonMethods.showAlert(
                            context,
                            "Alert !",
                            intent.getStringExtra("message")
                        )
                    }
                }
            }
        }
        registerBroadCastReciever()

        if (FreeRoadingPreferenceManager.getInstance().getRideStatus() != null)
            if (FreeRoadingPreferenceManager.getInstance().getRideStatus().equals("Edit")) {
                bookingId = FreeRoadingPreferenceManager.getInstance().appointmentId
                initRideDetailsOnRequestPage()
                FreeRoadingPreferenceManager.getInstance().setRideStatus("On")
                updateMapAfterRecieverStatus(4)
                beginJourneySuccessfully("Success")
            }
    }

    private fun getRideStatusResponse(homeData: Home_data) {
        if (homeData.getStatus().equals("5")) {
            // open arrived page
            bookingId = homeData.getApp_appointment_id()
            displayBookingResponseAfterHomeStatus(homeData)
            hideAndShowLayout("driver_on_the_way")
            publishDataOnPubnubWithIntervals(
                "5",
                "freeroading_" + homeData.getPassenger_device_id()
            )
            updateMapAfterRecieverStatus(3)
            FreeRoadingPreferenceManager.getInstance().setRideStatus("On")
        } else if (homeData.getStatus().equals("4")) {
            // open start journey page
            bookingId = homeData.getApp_appointment_id()
            displayBookingResponseAfterHomeStatus(homeData)
            publishDataOnPubnubWithIntervals(
                "5",
                "freeroading_" + homeData.getPassenger_device_id()
            )
            iHaveArrivedSuccefully("Success")
            FreeRoadingPreferenceManager.getInstance().setRideStatus("On")
        } else if (homeData.getStatus().equals("3")) {
            // open dropped journey page
            bookingId = homeData.getApp_appointment_id()
            displayBookingResponseAfterHomeStatus(homeData)
            publishDataOnPubnubWithIntervals(
                "5",
                "freeroading_" + homeData.getPassenger_device_id()
            )
            updateMapAfterRecieverStatus(4)
            beginJourneySuccessfully("Success")
            FreeRoadingPreferenceManager.getInstance().setRideStatus("On")
        } else if (homeData.getStatus().equals("2")) {
            // open payment page
            //bookingId=homeData.getApp_appointment_id();
            val notifyManager =
                requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notifyManager.cancelAll()
            FreeRoadingPreferenceManager.getInstance().setRideStatus("On")
            (context as? BaseActivity)?.changeFragmentWithTag(
                PaymentFragment.getInstance(homeData),
                PaymentFragment.TAG
            );
            (context as DashboardActivity).setEnableDisableDrawer(false);
        } else if (homeData.getStatus().equals("6")) {
            bookingId = homeData.getApp_appointment_id()
            FreeRoadingPreferenceManager.getInstance().appointmentId = bookingId
            startRideRequestPage()
            mRideRequest = true
            FreeRoadingPreferenceManager.getInstance().setEstimatedTime("0")
        }
    }

    private lateinit var mobileNo: String

    private fun displayBookingResponseAfterHomeStatus(rideHistory: Home_data) {

        tv_ride_request_booking_id.setText("Booking Id : $bookingId")
        tv_pick_up_address.setText(rideHistory.getPick_address())
        location.setText(rideHistory.getPick_address())
        tv_arrival_distance.setText(rideHistory.getDistance_in_mts() + " Miles")

        mCustomerLat = rideHistory.getPick_latitude()
        mCustomerLng = rideHistory.getPick_longitude()

        mDropOffLat = rideHistory.getDrop_latitude()
        mDropOffLng = rideHistory.getDrop_longitude()

        mPickAddress = rideHistory.getPick_address()
        mDropAddress = rideHistory.getDrop_address()
        mobileNo = rideHistory.mobile
        GlideUtil.loadImageWithDefaultImage(
            context as BaseActivity,
            iv_profile,
            URLConstant.PASSENGER_IMAGE_URL + rideHistory.getProfile_pic(),
            R.drawable.ic_profile_placeholder
        )

        tv_journy_started_bookingid.setText("Booking Id : $bookingId")
        tv_pick_up_location.setText(rideHistory.getPick_address())
        tv_drop_off_location.setText(rideHistory.getDrop_address())
        tv_driver_name.setText(rideHistory.getCustomer_name())
//        tv_customer_location.setText(rideHistory.getPick_address())
    }

    private fun beginJourneySuccessfully(success: String) {
        if (success == "Success") {
            googleMap
            hideAndShowLayout("dropped_passenger")
            updateMapAfterRecieverStatus(5)
            updateIOSPassenegr("3")
        }
    }

    private fun passengerDroppedSuccefully(responseData: ResponseData) {
        updateIOSPassenegr("2")
        bookingId = ""
        FreeRoadingPreferenceManager.getInstance().appointmentId = ""
        val notifyManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notifyManager.cancelAll()
        rideRejectedSuccefully()
        (context as? BaseActivity)?.changeFragmentWithTag(
            PaymentFragment.getInstance(responseData.home_data),
            PaymentFragment.TAG
        )
        (context as DashboardActivity).setEnableDisableDrawer(false);

    }

    private fun iHaveArrivedSuccefully(status: String) {
        if (status == "Success") hideAndShowLayout("i_have_arrived")
        updateMapAfterRecieverStatus(4)
        updateIOSPassenegr("4")
    }

    private fun rideRejectedSuccefully() {
        hideAndShowLayout("")
        bookingId = ""
        drawMarker(LatLng(gpsTracker.location.getLatitude(), gpsTracker.location.getLongitude()))
        if (requestInterval != null) {
            requestInterval!!.cancel()
        }
        /* NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();*/
    }

    private fun hideAndShowLayout(pageName: String) {
        if (pageName == "ride_request") {
            try {
                (context as DashboardActivity).setToolbarTitle(getString(R.string.header_ride_request))
                ride_requestHolder.setVisibility(View.VISIBLE)
                ontheWayHolder.setVisibility(View.GONE)
                ll_start_journy_driver_action.setVisibility(View.GONE)
                ll_drop_journy_driver_action.setVisibility(View.GONE)
                hideAndShowNavigationDrawer(false)
                mRideRequest = false
            } catch (e: Exception) {
                e.printStackTrace()
            }


        } else if (pageName == "driver_on_the_way") {
            try {
                (context as DashboardActivity).setToolbarTitle(getString(R.string.header_driver_on_way))
                ontheWayHolder.setVisibility(View.VISIBLE)
                ll_onrequest_driver_action.setVisibility(View.VISIBLE)
                ride_requestHolder.setVisibility(View.GONE)
                ll_start_journy_driver_action.setVisibility(View.GONE)
                ll_drop_journy_driver_action.setVisibility(View.GONE)
//            estimated_time_tv.setVisibility(View.VISIBLE)
//            estimated_time_tv.setText(String.format(getString(R.string.estimed_time), FreeRoadingPreferenceManager.getInstance().getEstimatedTime()))
//            checkDriverGeofence()
                hideAndShowNavigationDrawer(false)
            } catch (e: Exception) {
                e.printStackTrace()
            }


        } else if (pageName == "i_have_arrived") {
            try {
                (context as DashboardActivity).setToolbarTitle(getString(R.string.header_begin_journey))
                ontheWayHolder.setVisibility(View.VISIBLE)
                ll_start_journy_driver_action.setVisibility(View.VISIBLE)
                ride_requestHolder.setVisibility(View.GONE)
                ll_onrequest_driver_action.setVisibility(View.GONE)
                ll_drop_journy_driver_action.setVisibility(View.GONE)
                hideAndShowNavigationDrawer(false)
            } catch (e: Exception) {
                e.printStackTrace()
            }


        } else if (pageName == "dropped_passenger") {
            try {
                (context as DashboardActivity).setToolbarTitle(getString(R.string.header_dropped_journey))
                ontheWayHolder.setVisibility(View.VISIBLE)
                ll_start_journy_driver_action.setVisibility(View.GONE)
                ride_requestHolder.setVisibility(View.GONE)
                ll_onrequest_driver_action.setVisibility(View.GONE)
                ll_drop_journy_driver_action.setVisibility(View.VISIBLE)
                hideAndShowNavigationDrawer(false)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else {
            try {
                (context as DashboardActivity).setToolbarTitle(getString(R.string.menu_home))
                ontheWayHolder.setVisibility(View.GONE)
                ll_start_journy_driver_action.setVisibility(View.GONE)
                ride_requestHolder.setVisibility(View.GONE)
                ll_onrequest_driver_action.setVisibility(View.GONE)
                ll_drop_journy_driver_action.setVisibility(View.GONE)
                hideAndShowNavigationDrawer(true)
                if (timer != null) timer!!.cancel()
                initMap(gpsTracker.location.latitude, gpsTracker.location.longitude)

                FreeRoadingPreferenceManager.getInstance().setRideStatus("Off")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun hideAndShowNavigationDrawer(status: Boolean) {
        try {
            (context as DashboardActivity).setEnableDisableDrawer(status)
            (context as DashboardActivity).toolbar.navigationIcon = null

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    internal var handler = Handler()
    private fun updateMapAfterRecieverStatus(actionId: Int) {
        mCurrentLat = "" + gpsTracker.location.getLatitude()
        mCurrentLng = "" + gpsTracker.location.getLongitude()

        if (requestInterval != null) requestInterval!!.cancel()
        when (actionId) {
            // when driver on the way
            3 -> if (mCustomerLat != "" && mCustomerLng != "") {
                clearMap()
                handler.postDelayed({
                    try {
                        val latLng = LatLng(
                            java.lang.Double.parseDouble(mCurrentLat),
                            java.lang.Double.parseDouble(mCurrentLng)
                        )
                        googleMap!!.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                latLng,
                                AppConstant.MAP_ZOOM
                            )
                        )
                        markerMapOnTheWay = googleMap!!.addMarker(
                            MarkerOptions().position(latLng)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ci_car_available))
                                .rotation(0f).flat(false)
                        )
                        val url = getMapsApiDirectionsUrl(mCustomerLat!!, mCustomerLng!!)
                        //     val url = getMapsApiDirectionsFromToUrl()
                        val downloadTask = ReadTask("direction", "")
                        downloadTask.execute(url)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }, 2000)

                handler.postDelayed({ updateDriverLocationOnRoute("ontheway") }, 7000)

            }
            // when driver arrived
            4 -> if (mDropOffLat != "" && mDropOffLng != "") {

                clearMap()
                handler.postDelayed({
                    try {
                        val latLng = LatLng(
                            java.lang.Double.parseDouble(mCurrentLat),
                            java.lang.Double.parseDouble(mCurrentLng)
                        )
                        googleMap!!.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                latLng,
                                AppConstant.MAP_ZOOM
                            )
                        )
                        markerMapOnTheWay = googleMap!!.addMarker(
                            MarkerOptions().position(latLng)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ci_car_available))
                                .rotation(0f).flat(false)
                        )
                        val url = getMapsApiDirectionsFromToUrl()
                        val downloadTask = ReadTask("direction", "")
                        downloadTask.execute(url)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }, 2000)
            }

            // when journey started
            5 -> if (mCurrentLat != "" && mCurrentLng != "") {
                try {
                    clearMap()
                    updateDriverLocationOnRoute("journeystarted")
                    val url = getMapsApiDirectionsUrl(mDropOffLat!!, mDropOffLng!!)
                    val downloadTask = ReadTask("direction", "")
                    downloadTask.execute(url)
                } catch (e: Exception) {

                }

            }
        }
    }

    internal var TIME_OUT: Int = 0
    internal var counter: Int = 0
    internal var requestInterval: Timer? = null
    private fun updateDriverLocationOnRoute(driverStatus: String) {
        try {
            TIME_OUT = 55000
            counter = 0
            if (requestInterval != null) {
                requestInterval!!.cancel()
            }
            requestInterval = Timer()
            val taskToDo = object : TimerTask() {
                override fun run() {
                    counter = counter + 5000
                    (context as? BaseActivity)?.runOnUiThread {
                        UpdateDriverLocation_DriverOnTheWay(
                            driverStatus
                        )
                    }
                }
            }
            requestInterval!!.scheduleAtFixedRate(taskToDo, 100, 10000)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Updating driver location when Driver On The Way
     */
    private fun UpdateDriverLocation_DriverOnTheWay(rideStatus: String) {
        val driver_current_latitude = gpsTracker.location.getLatitude()
        val driver_cuttent_longitude = gpsTracker.location.getLongitude()
        try {
            val latLng = LatLng(driver_current_latitude, driver_cuttent_longitude)
            animateMarkerNew(latLng, markerMapOnTheWay, rideStatus)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, e.toString())
        }

    }

    private fun getMapsApiDirectionsUrl(customer_lat: String, customer_long: String): String {

        try {
            val latLng = LatLng(
                java.lang.Double.parseDouble(mCustomerLat),
                java.lang.Double.parseDouble(mCustomerLng)
            )
            googleMap!!.addMarker(
                MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ci_location_pin))
            )
            //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, AppConstant.MAP_ZOOM));

        } catch (e: Exception) {
            e.printStackTrace()
        }
        val waypoints = ("origin=" + mCurrentLat + "," + mCurrentLng + "&" + "destination="
                + customer_lat + "," + customer_long)

        val sensor = "sensor=false"
        val params = "$waypoints&$sensor"
        val output = "json"
        val key = "&key=" + getString(R.string.map_key)
        return "https://maps.googleapis.com/maps/api/directions/$output?$params$key"
    }

    /**
     * To get the directions url between pickup location and drop location
     */
    private fun getMapsApiDirectionsFromToUrl(): String {
        try {
            val latLngDrop = LatLng(
                java.lang.Double.parseDouble(mDropOffLat),
                java.lang.Double.parseDouble(mDropOffLng)
            )

            googleMap!!.addMarker(
                MarkerOptions().position(latLngDrop)
                    // .title("Drop Location")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ci_location_pin))
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val waypoints = ("origin=" + mCurrentLat + "," + mCurrentLng + "&" + "destination="
                + mDropOffLat + "," + mDropOffLng)

        val sensor = "sensor=false"
        val params = "$waypoints&$sensor"
        val output = "json"
        val key = "&key=" + getString(R.string.map_key)
        return "https://maps.googleapis.com/maps/api/directions/$output?$params$key"
    }

    /**
     * method used to read the url go for parse it
     */

    internal inner class ReadTask(requestFor: String, estimatedDuration: String) :
        AsyncTask<String, Void, String>() {

        var requestFor = ""
        var estimatedDuration = ""

        init {
            this.requestFor = requestFor
            this.estimatedDuration = estimatedDuration
        }

        override fun doInBackground(vararg url: String): String {
            var data = ""
            try {
                val http = HTTPOperationController()
                data = http.readUrl(url[0])
            } catch (e: Exception) {
                println("Background Task" + e.toString())
            }

            return data
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            if (requestFor == "direction")
                ParserTask().execute(result)
            else
                parseDistanceResponse(result, estimatedDuration)
        }
    }

    private fun parseDistanceResponse(jsonData: String, estimatedDuration: String) {
        try {
            val parser = PathJSONParser()
//            if(parser.parseDistanceResponse(JSONObject(jsonData).get("value")!=null))
            val distance = parser.parseDistanceResponse(JSONObject(jsonData))
            if (estimatedDuration != "estimatedTime") {
                if (Integer.parseInt(distance.get(1)) > 48280) {
//                    ll_distance_layout.setVisibility(View.VISIBLE)
                    tv_arrival_distance.setText(distance.get(0))
//                    tv_arrival_timing.setText(distance.get(2))
                } else {
//                    ll_distance_layout.setVisibility(View.INVISIBLE)
                }
                val distancePath =
                    "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=$mCustomerLat,$mCustomerLng+&destinations=$mCurrentLat,$mCurrentLng"
                val downloadTask = ReadTask("distance", "estimatedTime")
                downloadTask.execute(distancePath)
            } else {
                System.out.println(
                    "Estimated time==" + DateFormatter.secToTime(
                        Integer.parseInt(
                            distance.get(3)
                        )
                    )
                )
                FreeRoadingPreferenceManager.getInstance()
                    .setEstimatedTime(DateFormatter.secToTime(Integer.parseInt(distance.get(3))))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * plotting the directions in google map
     */
    internal inner class ParserTask :
        AsyncTask<String, Int, List<List<HashMap<String, String>>>>() {
        override fun doInBackground(vararg jsonData: String): List<List<HashMap<String, String>>>? {
            val jObject: JSONObject
            var routes: List<List<HashMap<String, String>>>? = null

            try {
                jObject = JSONObject(jsonData[0])
                val parser = PathJSONParser()
                routes = parser.parse(jObject)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return routes
        }

        override fun onPostExecute(routes: List<List<HashMap<String, String>>>) {
            var points: ArrayList<LatLng>? = null
            var polyLineOptions: PolylineOptions? = null

            // traversing through routes
            for (i in routes.indices) {
                points = ArrayList()
                polyLineOptions = PolylineOptions()
                val path = routes[i]

                for (j in path.indices) {
                    val point = path[j]

                    val lat = java.lang.Double.parseDouble(point["lat"])
                    val lng = java.lang.Double.parseDouble(point["lng"])
                    val position = LatLng(lat, lng)
                    points.add(position)
                }
                polyLineOptions.addAll(points)
                polyLineOptions.width(15f)
                polyLineOptions.color(Color.RED)
            }

            if (polyLineOptions != null)
                googleMap!!.addPolyline(polyLineOptions)
        }
    }

    //Method for finding bearing between two points
    private fun getBearing(begin: LatLng, end: LatLng): Float {
        val lat = Math.abs(begin.latitude - end.latitude)
        val lng = Math.abs(begin.longitude - end.longitude)

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return Math.toDegrees(Math.atan(lng / lat)).toFloat()
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (90 - Math.toDegrees(Math.atan(lng / lat)) + 90).toFloat()
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (Math.toDegrees(Math.atan(lng / lat)) + 180).toFloat()
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (90 - Math.toDegrees(Math.atan(lng / lat)) + 270).toFloat()
        return -1f
    }

    /**
     * To get the directions url between pickup location and driver current
     * location
     *
     * @param driver_lat
     * @param driver_long
     * @return DirectionsUrl
     */
    private var mDropOffLat: String? = null
    private var mDropOffLng: String? = null
    private var mCustomerLat: String? = null
    private var mCustomerLng: String? = null
    private var mDropAddress: String? = null
    private var mPickAddress: String? = null
    internal var mGeofenceTimer: Timer? = null
    private fun checkDriverGeofence() {

        if (mGeofenceTimer != null) mGeofenceTimer!!.cancel()
        mGeofenceTimer = Timer()
        mGeofenceTimer!!.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                (context as BaseActivity).runOnUiThread {
                    try {
                        val distanceInMeters = LocationUtil.getDistanceInMeters(
                            LocationUtil.distanceBetweenTwoPoint(
                                java.lang.Double.parseDouble(mCustomerLat),
                                java.lang.Double.parseDouble(mCustomerLng),
                                gpsTracker.location.getLatitude(),
                                gpsTracker.location.getLongitude()
                            )
                        )
                        //Toast.makeText(context,"Driver Distance from Pickup Location == "+distanceInMeters + "Mtr",Toast.LENGTH_LONG).show();
                        if (distanceInMeters.toInt() <= 92) {
//                        btn_ihve_arrived.setVisibility(View.VISIBLE)
                            if (mGeofenceTimer != null) mGeofenceTimer!!.cancel()
                        } else {
//                        btn_ihve_arrived.setVisibility(View.GONE)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }, 100, 10000)
    }


    private fun replaceFragment(fragment: BaseFragment, TAG: String) {
        if (context is BaseActivity) {
            (context as BaseActivity).changeFragmentWithTag(fragment, TAG)
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            requireActivity().unregisterReceiver(onRideRequestReciever)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onStop() {
        super.onStop()
        try {
            if (onRideRequestReciever != null)
                requireActivity().unregisterReceiver(onRideRequestReciever)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        try {
            if (onRideRequestReciever != null)
                requireActivity().unregisterReceiver(onRideRequestReciever)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateIOSPassenegr(status: String) {
        try {
            val mObject = JsonObject()
            mObject.addProperty("rq_tp", "5")
            mObject.addProperty("lt", gpsTracker.location.getLatitude())
            mObject.addProperty("lg", gpsTracker.location.getLongitude())
            mObject.addProperty("st", status)
            mObject.addProperty("email", UserManager.getInstance().user.email)
            mObject.addProperty("mobile", UserManager.getInstance().user.mobile)
            mObject.addProperty("name", UserManager.getInstance().user.name)
            mObject.addProperty("appointment_id", bookingId)
            PubNubManager.getPubnubManager().publish(mObject, passengerChannal)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }


    var statusAdapter: StatusAdapter? = null
    fun setStatusAdapter(status: Status) {

        var list: ArrayList<String>? = ArrayList<String>()
        if (status.stripe_status == "False") {
            FreeRoadingPreferenceManager.getInstance().setAddRideStatus(false)
            list!!.add(requireActivity().getString(R.string.status_stripe_connect))
        }
        if (status.admin_status == "False") {
            FreeRoadingPreferenceManager.getInstance().setAddRideStatus(false)
            list!!.add(requireActivity().getString(R.string.status_admin_aprovel))

        }

        if (status.backend_status == "False") {
            FreeRoadingPreferenceManager.getInstance().setAddRideStatus(false)
            list!!.add(requireActivity().getString(R.string.status_backend_approval))
        }

        if (status.vehicle_status == "False") {
            FreeRoadingPreferenceManager.getInstance().setAddRideStatus(false)
            list!!.add(requireActivity().getString(R.string.status_update_vehical_details))
        }
        if (status.profile_status == "False") {
            FreeRoadingPreferenceManager.getInstance().setAddRideStatus(false)
            list!!.add(requireActivity().getString(R.string.status_update_profile))
        }

        rv_status.visibility = View.VISIBLE
        if (list?.size == 0) {
            FreeRoadingPreferenceManager.getInstance().setAddRideStatus(true)
        } else {
            DialogUtil.showOkButtonDialog(
                context,
                getString(R.string.message),
                getString(R.string.please_complete_your_profile),
                object : DialogUtil.AlertDialogInterface.OneButtonDialogClickListener {
                    override fun onButtonClick() {

                    }
                })
        }
        statusAdapter = StatusAdapter(context, list, this)
        val mLayoutManager = LinearLayoutManager(context)
        rv_status.layoutManager = mLayoutManager
        rv_status.itemAnimator = DefaultItemAnimator()
        rv_status.setAdapter(statusAdapter)

    }

    override fun onStripeClick() {
        (context as BaseActivity)?.changeFragmentWithTag(
            StripeFragment.newInstance("", ""),
            StripeFragment.TAG
        )
        (context as DashboardActivity)?.setBackVisibility(true)
        (context as DashboardActivity)?.setToolbarTitle(getString(R.string.stripe_connect))

    }

    override fun onAdminClick() {

        DialogUtil.showToastShortLength(context, getString(R.string.contact_to_admin))
    }

    override fun backendClick() {
        //DialogUtil.showToastShortLength(context,getString(R.string.backend_verification))
    }

    override fun onVehicalClick() {
        FragmentFactory.replaceFragmentWithAnim(
            CarDetailsFragment(),
            R.id.fragment_container,
            context,
            TAG
        )
        (context as? DashboardActivity)?.setBackVisibility(true)
        (context as DashboardActivity).setToolbarTitle(getString(R.string.car_details))
    }

    override fun onProfileClick() {
        FragmentFactory.replaceFragmentWithAnim(
            ProfileFragment(),
            R.id.fragment_container,
            context,
            TAG
        )
        (context as? DashboardActivity)?.setBackVisibility(true)
        (context as DashboardActivity).setToolbarTitle(getString(R.string.car_details))
    }
}// Required empty public constructor
