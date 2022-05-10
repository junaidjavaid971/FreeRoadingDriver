package com.apps.freeroadingdriver.fragments

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.freeroading.fragments.BookingDetails
import com.app.freeroading.model.requestModel.TripFinishRequest
import com.app.freeroading.model.requestModel.TripStatusUpdateRequest
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.activities.DashboardActivity
import com.apps.freeroadingdriver.adapter.BookingRideAdapter
import com.apps.freeroadingdriver.constants.AppConstant
import com.apps.freeroadingdriver.constants.CommonMethods
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.manager.LocationManagerWIthGps
import com.apps.freeroadingdriver.model.dataModel.CancelForIndividual
import com.apps.freeroadingdriver.model.requestModel.CancelIndividualRideRequest
import com.apps.freeroadingdriver.model.requestModel.CancelRoadTripRequest
import com.apps.freeroadingdriver.model.requestModel.TripJourneyStartRequest
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import com.apps.freeroadingdriver.model.responseModel.Booking_data
import com.apps.freeroadingdriver.model.responseModel.Road_trips
import com.apps.freeroadingdriver.network.HTTPOperationController
import com.apps.freeroadingdriver.prefrences.FreeRoadingPreferenceManager
import com.apps.freeroadingdriver.requester.*
import com.apps.freeroadingdriver.route.GetDirectionsData
import com.apps.freeroadingdriver.utils.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_active_ride_details.*
import kotlinx.android.synthetic.main.progress_bar_layout.*
import org.greenrobot.eventbus.Subscribe
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ActiveRideDetailsFragment : BaseFragment(), OnMapReadyCallback, View.OnClickListener, DialogUtil.AlertDialogInterface.OneButtonDialogClickListener, BookingRideAdapter.BookingListener {
    internal var cl = Calendar.getInstance()

    private var sDate: String? = null
    internal var sdf = SimpleDateFormat("yyyy-MM-dd")
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.cancelRidee -> {
                DialogUtil.showTwoButtonDialog((context as DashboardActivity),getString(R.string.are_you_sure), "Yes", "No", object:DialogUtil.AlertDialogInterface.TwoButtonDialogClickListener{
                    override fun onPositiveButtonClick() {
                        CommonUtil.showProgressBar(rl_progress_bar)
                        BackgroundExecutor.instance.execute(CancelRoadTripRequester(CancelRoadTripRequest(ride_id!!, CommonMethods.getCurrentDateOnly(), CommonMethods.getTimeZone())))
                    }

                    override fun onNegativeButtonClick() {
                    }

                })
            }
            R.id.startJourney -> {

                if (bookingList.size > 0) {
                    if (bookingList[0].trip_date!!.equals(CommonMethods.getCurrentDateOnly()))
                    {
                        Log.d("MyApp", sDate.toString())
                        CommonUtil.showProgressBar(rl_progress_bar)
                        BackgroundExecutor.instance.execute(TripJourneyStartedRequester(TripJourneyStartRequest(ride_id!!,
                                CommonMethods.getCurrentDateOnly(), CommonMethods.getTimeZone(), "" + tracker?.getLocation()?.latitude,
                                "" + tracker?.getLocation()?.longitude)))
                    }
                    else{
                        CommonMethods.showShortToast(context,"Not allowed to start journey in different date.")
                    }

                } else {
                    DialogUtil.showToastLongLength(context, "No Booked Passenger")
                }

            }
            R.id.finishJourney -> {
                CommonUtil.showProgressBar(rl_progress_bar)
                BackgroundExecutor.instance.execute(TripFinishRequester(TripFinishRequest(ride_id!!,
                        CommonMethods.getCurrentDateOnly(), CommonMethods.getTimeZone(),
                        MapUtils.getAddressFromLatLng(requireContext(), tracker?.getLocation()?.latitude!!, tracker?.getLocation()?.longitude!!),
                        "" + tracker?.getLocation()?.latitude,
                        "" + tracker?.getLocation()?.longitude)))
            }
        }
    }

    override fun onButtonClick() {
        CommonUtil.showProgressBar(rl_progress_bar)
        BackgroundExecutor.instance.execute(CancelRoadTripRequester(CancelRoadTripRequest(ride_id!!, CommonMethods.getCurrentDateOnly(), CommonMethods.getTimeZone())))
    }

    // TODO: Rename and change types of parameters
    private var road_trips: Road_trips? = null
    private var param2: String? = null
    private var ride_id: String? = null
    private var lattitude: Double = 0.0
    private var longitude: Double = 0.0
    private var drop_status: String? = null
    private var tracker: GPSTracker? = null
    private var googleMap: GoogleMap? = null
    internal lateinit var locationManager: LocationManagerWIthGps
    private lateinit var bookingList: ArrayList<Booking_data>
    lateinit var activeRideListAdapter: BookingRideAdapter
    private var markerMapOnTheWay: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            road_trips = it.getParcelable<Road_trips>(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_active_ride_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sDate = sdf.format(cl.time)

        bookingList = ArrayList()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        locationManager = LocationManagerWIthGps.getInstance()
        Handler().postDelayed(Runnable { setDataOnView(road_trips) }, 500)
        cancelRidee.setOnClickListener(this)
        startJourney.setOnClickListener(this)
        finishJourney.setOnClickListener(this)

        (context as DashboardActivity).iv_details.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                (context as DashboardActivity).changeFragment(BookingDetails.newInstance(road_trips!!, ""), BookingDetails.TAG);
                (context as DashboardActivity).setEnableDisableDrawer(flag = false)
                (context as DashboardActivity).setBackVisibility(true)
                (context as DashboardActivity).setToolbarTitle("Active Ride Details")
            }


        })
    }

    private fun setDataOnView(road_trips: Road_trips?) {
        try {
            timeJourney.text = CommonMethods.returnNotnullString(road_trips?.departure_time)
            priceJorney.text = CommonMethods.returnNotnullString(AppConstant.CURRENCY_UNIT + road_trips?.how_much_ride_amount)
            ride_id = road_trips?.id
            bookingId.text = CommonMethods.returnNotnullString("Trip ID : " + road_trips?.id)
            tv_date.text = CommonMethods.returnNotnullString(road_trips?.departure_date!!.split(" ").get(0))

            tv_total_rider.text = road_trips?.number_of_riders
            tv_booked_rider.text = road_trips?.booked_seat
            tv_drop_off_location_address.text = road_trips?.droppoff_location
            tv_pick_up_address.text = road_trips?.pickup_location

            val position = LatLng(java.lang.Double.parseDouble(road_trips?.ride_pick_latitude!!), java.lang.Double.parseDouble(road_trips?.ride_pick_longitude!!))
            googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 12.0f))
            val dataTransfer = arrayOfNulls<Any>(3)
            val url = MapUtils.Companion.getMapsApiDirectionsUrl(road_trips?.ride_drop_latitude!!, road_trips?.ride_drop_longitude!!,
                    road_trips?.ride_pick_latitude!!, road_trips?.ride_pick_longitude!!, googleMap!!)
            val directionsData = GetDirectionsData()
            dataTransfer[0] = googleMap
            dataTransfer[1] = url
//                dataTransfer[2] = new LatLng(latitude,longitude);
            directionsData.execute(*dataTransfer)

            if (road_trips!!.trip_status.equals("3")) {
                finishJourney.visibility = View.VISIBLE
                bottomHolder.visibility = View.GONE
            } else if (road_trips!!.trip_status.equals("1")) {
                finishJourney.visibility = View.GONE
                bottomHolder.visibility = View.GONE
                DialogUtil.showOkButtonDialog(context, "Alert", getString(R.string.ride_completed), "")
            }

            if (road_trips.booking_data != null && road_trips.booking_data!!.size > 0) {
                bookingList.addAll(road_trips.booking_data!!)
                setAdapter(false, road_trips.trip_status!!)

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        this.googleMap = p0
        initLocation()
    }

    companion object {
        val TAG = ActiveRideDetailsFragment::class.java.name

        @JvmStatic
        fun newInstance(param1: Road_trips, param2: String) =
                ActiveRideDetailsFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }


    private var postion: Int? = 0
    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        if (eventObject == null) {
            return
        }
        requireActivity().runOnUiThread(Runnable {
            try {
                CommonUtil.hideProgressBar(rl_progress_bar)
                onHandleBaseEvent(eventObject)
                when (eventObject.id) {
                    EventConstant.CALL_PASSENGER -> {
                        val response = eventObject.`object` as String
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:" + response)
                        startActivity(intent)
                    }
                    EventConstant.CANCEL_INDIVIDUAL_RIDE -> {
                        val response = eventObject.`object` as CancelForIndividual
                        CommonUtil.showProgressBar(rl_progress_bar)
                        BackgroundExecutor.instance.execute(CancelIndividualRideRequester(CancelIndividualRideRequest
                        (response.appointmetId, CommonMethods.getCurrentDateOnly(), CommonMethods.getTimeZone(), "Late", "1")))//1 for driver, 2 for passenger
                        postion = response.position
                    }
                    EventConstant.I_HAVE_ARRIVED -> {
                        val response = eventObject.`object` as CancelForIndividual
                        CommonUtil.showProgressBar(rl_progress_bar)
                        drop_status = response.status
                        if (!drop_status.equals("1")) {
                            BackgroundExecutor.instance.execute(TripStatusUpdateRequester(TripStatusUpdateRequest(response.appointmetId,
                                    CommonMethods.getCurrentDateOnly(), CommonMethods.getTimeZone(),
                                    MapUtils.getAddressFromLatLng(requireContext(), tracker?.getLocation()?.latitude!!, tracker?.getLocation()?.longitude!!),
                                    "" + tracker?.getLocation()?.latitude,
                                    "" + tracker?.getLocation()?.longitude, response.status)))
                        } else {
                            (context as DashboardActivity).changeFragmentWithTag(TripInvoiceFragment.newInstance(response.appointmetId, response.driverType), ActiveRideDetailsFragment.TAG)
                            (context as DashboardActivity).setEnableDisableDrawer(flag = false)
                            (context as DashboardActivity).setBackVisibility(true)
                            (context as DashboardActivity).setToolbarTitle(getString(R.string.invoice))
                        }
                        postion = response.position
                    }
                    EventConstant.CANCELINDIVIDUAL_SUCCESS -> {
                        Log.d(BaseFragment.TAG, "cancel success")
                        val response = eventObject.`object` as BaseResponse
                        CommonMethods.showLongToast(context, response.response_msg)
                        bookingList.removeAt(postion!!)
                        activeRideListAdapter.notifyDataSetChanged()
                    }
                    EventConstant.CANCELROADTRIP_SUCCESS -> {
                        Log.d(BaseFragment.TAG, "cancel success")
                        val response = eventObject.`object` as BaseResponse
                        CommonMethods.showLongToast(context, response.response_msg)
                        FragmentFactory.removedBack(context)
                    }
                    EventConstant.TRIPJOURNEY_SUCCESS -> {
                        Log.d(BaseFragment.TAG, "journeystarted success")
                        val response = eventObject.`object` as BaseResponse
                        CommonMethods.showLongToast(context, response.response_msg)
                        finishJourney.visibility = View.VISIBLE
                        bottomHolder.visibility = View.GONE
                        FreeRoadingPreferenceManager.getInstance().rideActive = true
                        FreeRoadingPreferenceManager.getInstance().rideRoadType = true
                        FreeRoadingPreferenceManager.getInstance().setServerChannel(response.response_data!!.ser_chn)
                        FreeRoadingPreferenceManager.getInstance().setPubNubChan(response.response_data!!.pub_chn)
                        setAdapter(true, "3")
                    }
                    EventConstant.TRIPSTATUSUPDATE_SUCCESS -> {
                        Log.d(BaseFragment.TAG, "tripstatus success")
                        val response = eventObject.`object` as BaseResponse
                        CommonMethods.showLongToast(context, response.response_msg)
                        if (drop_status.equals(AppConstant.DROP_STATUS)) {
                            setAdapter(true, "3")
                            (context as DashboardActivity).changeFragmentWithTag(TripInvoiceFragment.newInstance
                            (response.response_data?.booking_data?.get(0)?.app_appointment_id!!,
                                    response.response_data?.booking_data?.get(0)?.driver_type!!), ActiveRideDetailsFragment.TAG)
                            (context as DashboardActivity).setEnableDisableDrawer(flag = false)
                            (context as DashboardActivity).setBackVisibility(true)
                            (context as DashboardActivity).setToolbarTitle(getString(R.string.invoice))
                        } else {
                            bookingList.clear()
                            bookingList.addAll(response.response_data?.booking_data!!)
                            setAdapter(true, "3")
                        }
                    }
                    EventConstant.TRIPEND_SUCCESS -> {
                        Log.d(BaseFragment.TAG, "tripstatus success")
                        val response = eventObject.`object` as BaseResponse
                        CommonMethods.showLongToast(context, response.response_msg)
                        FreeRoadingPreferenceManager.getInstance().rideActive = false
                        FreeRoadingPreferenceManager.getInstance().rideRoadType = false
                        FragmentFactory.removedBack(context)
                        (context as DashboardActivity).setEnableDisableDrawer(true)
                    }

                    EventConstant.CANCELINDIVIDUAL_ERROR, EventConstant.CANCELROADTRIP_ERROR, EventConstant.TRIPJOURNEY_ERROR,
                    EventConstant.TRIPSTATUSUPDATE_ERROR, EventConstant.TRIPEND_ERROR -> {
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

    private fun initLocation() {
        tracker = GPSTracker(context)

        if (locationManager.latitude === 0.0) {
            lattitude = tracker!!.getLocation()!!.latitude
            longitude = tracker!!.getLocation()!!.longitude
        } else {
            lattitude = locationManager.latitude
            longitude = locationManager.longitude
        }
        MapUtils.initMap(requireContext(), lattitude, longitude, googleMap)
    }


    private fun setAdapter(isJourneyStarted: Boolean, rideStatus: String) {

        activeRideListAdapter = BookingRideAdapter(context, bookingList, isJourneyStarted, rideStatus, this)
        val mLayoutManager = LinearLayoutManager(context)
        bookingView.setLayoutManager(mLayoutManager)
        bookingView.setItemAnimator(DefaultItemAnimator())
        bookingView.setAdapter(activeRideListAdapter)
    }

    override fun onCustomerclicked(postition: Int) {
        //  Toast.makeText(activity,"Coming soon", Toast.LENGTH_SHORT).show()
        (context as DashboardActivity).changeFragmentWithTag(Customer_Details.newInstance(bookingList.get(postition), ""), ActiveRideDetailsFragment.TAG)
        (context as DashboardActivity).setEnableDisableDrawer(flag = false)
        (context as DashboardActivity).setBackVisibility(true)
        (context as DashboardActivity).iv_details.visibility = View.GONE
        (context as DashboardActivity).setToolbarTitle("Customer Details")
    }


    override fun onResume() {
        super.onResume()
        (context as DashboardActivity).iv_details.visibility = View.VISIBLE
        (context as DashboardActivity).setToolbarTitle("Active Ride Detail")

    }

    override fun onPause() {
        super.onPause()
        (context as DashboardActivity).iv_details.visibility = View.GONE

    }

    private fun drawJorneyStartedRoute() {
        googleMap!!.clear()


        Handler().postDelayed({
            try {

                val latLng = LatLng(java.lang.Double.parseDouble(road_trips!!.ride_pick_latitude), java.lang.Double.parseDouble(road_trips!!.ride_drop_longitude))
                googleMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, AppConstant.MAP_ZOOM))
                markerMapOnTheWay = googleMap!!.addMarker(MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ci_car_available)).rotation(0f).flat(false))

                val url = getMapsApiDirectionsUrl(road_trips!!.ride_drop_latitude!!, road_trips!!.ride_drop_longitude!!)
                val downloadTask = ReadTask("direction")
                downloadTask.execute(url)


            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
        }, 500)
    }


    internal inner class ReadTask(requestFor: String) : AsyncTask<String, Void, String>() {

        var requestFor = ""

        init {
            this.requestFor = requestFor
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
            if (requestFor == "direction") {
                ParserTask().execute(result)
            }

        }

        internal inner class ParserTask : AsyncTask<String, Int, List<List<HashMap<String, String>>>>() {
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

                var points: java.util.ArrayList<LatLng>? = null
                var polyLineOptions: PolylineOptions? = null

                // traversing through routes
                for (i in routes.indices) {
                    points = java.util.ArrayList()
                    polyLineOptions = PolylineOptions()
                    val path = routes[i]

                    /*  val point_di: HashMap<String, String>   = path.get(0)
                  if (point_di!=null)
                      remainDistance = ""+point_di.get("distance")

                  var point_du : HashMap<String, String>  = path.get(1)
                  if (point_du!=null)
                      remainTime = ""+point_du.get("duration")*/

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


    }

    fun getMapsApiDirectionsUrl(customer_lat: String, customer_long: String): String {

        val latLng = LatLng(java.lang.Double.parseDouble(road_trips!!.ride_pick_latitude), java.lang.Double.parseDouble(road_trips!!.ride_drop_longitude))
        googleMap!!.addMarker(MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ci_location_pin)))
        //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, AppConstant.MAP_ZOOM));

        val waypoints = ("origin=" + road_trips!!.ride_pick_latitude + "," + road_trips!!.ride_drop_longitude + "&" + "destination="
                + customer_lat + "," + customer_long)
        val sensor = "sensor=false"
        val params = "$waypoints&$sensor"
        val output = "json"
        val key = "&key=" + getString(R.string.map_key)

        return "https://maps.googleapis.com/maps/api/directions/$output?$params$key"


    }

}
