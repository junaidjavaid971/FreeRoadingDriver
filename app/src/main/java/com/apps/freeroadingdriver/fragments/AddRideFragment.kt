package com.apps.freeroadingdriver.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.constants.CommonMethods
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.model.requestModel.AddRoadTripRequest
import com.apps.freeroadingdriver.model.requestModel.BaseRequest
import com.apps.freeroadingdriver.model.requestModel.ModelRequest
import com.apps.freeroadingdriver.model.responseModel.*
import com.apps.freeroadingdriver.requester.*
import com.apps.freeroadingdriver.utils.CommonUtil
import com.apps.freeroadingdriver.utils.DialogUtil
import com.apps.freeroadingdriver.utils.FragmentFactory
import com.apps.freeroadingdriver.utils.MapUtils
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.libraries.places.compat.ui.PlaceAutocomplete
import kotlinx.android.synthetic.main.fragment_add_ride.*
import kotlinx.android.synthetic.main.progress_bar_layout.*
import kotlinx.android.synthetic.main.request_accept_reject_layout.*
import org.greenrobot.eventbus.Subscribe
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [AddRideFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddRideFragment : BaseFragment(), View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private var mParam1: String? = null
    private var mParam2: String? = null
    private var locaiton_type = 1
    private var pickup_lat = 0.0
    private var pickup_lng = 0.0
    private var drop_lat = 0.0
    private var drop_lng = 0.0
    private var pickUpCity = ""
    private var pickUpZip = ""
    private var dropCity = ""
    private var dropZip = ""
    internal var sdf = SimpleDateFormat("yyyy-MM-dd")
    internal var sdfTime = SimpleDateFormat("HH:mm")
    internal var cl = Calendar.getInstance()
    internal var cl1 = Calendar.getInstance()
    internal lateinit var da: DatePickerDialog
    internal lateinit var ta: TimePickerDialog
    private var sDate: String? = null
    private var sTime: String? = null
    internal var mYear = cl.get(Calendar.YEAR)
    internal var mMonth = cl.get(Calendar.MONTH)
    internal var mDay = cl.get(Calendar.DAY_OF_MONTH)
    internal var mHour = cl1.get(Calendar.HOUR)
    internal var mMin = cl1.get(Calendar.MINUTE)
    var selected_vehicle: Int? = null
    var selected_year: String? = null
    var selected_vehicle_makeid: Int? = null
    var selected_vehicle_modelid: Int? = null
    lateinit var list_years: ArrayList<String>
    lateinit var vehicle_make: Array<Vehicle_make>;
    lateinit var vehicle_type: Array<Vehicle_type>;
    lateinit var vehicle_model: Array<Vehicle_model>;
    private val mDateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            mYear = year
            mMonth = monthOfYear + 1
            mDay = dayOfMonth
            updateDisplay()
            /*   if (mDay != dayOfMonth) {
                   mDay = dayOfMonth
                   updateDisplay()
               } else {
                   CommonMethods.showShortToast(context, "Please Select another day")
               }*/
        }

    private val mTimeListener = TimePickerDialog.OnTimeSetListener() { view, hour, min ->
        mHour = hour
        mMin = min
        var time: String? = null
        if (hour >= 0 && hour < 12) {
            time = String.format("%02d", mHour) + ":" + String.format("%02d", min) + " AM";
        } else {
            if (hour == 12) {
                time = String.format("%02d", mHour) + ":" + String.format("%02d", min) + " PM";

            } else {
                mHour = mHour - 12;
                time = String.format("%02d", mHour) + ":" + String.format("%02d", min) + " PM";
            }
        }

        txt_time.text = time
        //  updateDisplayTime()
    }

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
        return inflater.inflate(R.layout.fragment_add_ride, container, false)
    }

    private fun fillDetails() {
        if (res != null) {
            vehicleType_edittext.setText(res!!.vehicle_type)
            txt_make.setText(res!!.vehicle_make_title)
            txt_year.setText(res!!.vehicle_year)
            txt_model.setText(res!!.vehicle_model_title)
            txt_door.setText(res!!.no_of_door)
            txt_vehicle_type.setText(res!!.vehicle_type)


            var vehicleYear = res!!.vehicle_year
            for (item in list_years.indices) {
                if (vehicleYear!!.equals(list_years[item])) {
                    spin_vehicle_year.setSelection(item)
                    selected_year = list_years[item]
                }
            }
            //vehicleYear_edittext.setText(res!!.vehicle_year)
            colorvehicle_edittext.setText(res!!.vehicle_color)
            licenceplateno_edittext.setText(res!!.license_plate_no)
            if (!res?.no_of_door!!.equals("")) {
                howmanyDoorsSpinner.setSelection(
                    resources.getStringArray(R.array.doors).indexOf(res?.no_of_door)
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list_years = ArrayList()
        for (i in 1999..mYear) {
            list_years.add("" + i)
            print("yearrr" + list_years)
        }
        val v_years =
            ArrayAdapter<String>(
                requireActivity(),
                R.layout.spinner_bigtext_items,
                R.id.textvw,
                list_years
            );
        spin_vehicle_year?.adapter = v_years
        spin_vehicle_year?.setSelection(0)

        spin_vehicle_year.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                selected_year = selectedItem;
            } // to close the onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }



        hitCarDetailsApi()
        val door = ArrayAdapter<String>(
            requireActivity(),
            R.layout.spinner_bigtext_items,
            R.id.textvw,
            resources.getStringArray(R.array.doorsarray)
        );
        howmanyDoorsSpinner?.adapter = door
        val riders = ArrayAdapter<String>(
            requireActivity(),
            R.layout.spinner_bigtext_items,
            R.id.textvw,
            resources.getStringArray(R.array.riders)
        );
        spin_rider?.adapter = riders
        da = DatePickerDialog(
            requireActivity(), mDateSetListener,
            mYear, mMonth, mDay
        )
        sDate = sdf.format(cl.time)
        //txt_date.text = sDate
        //Get the DatePicker instance from DatePickerDialog
        val dp: DatePicker = da.getDatePicker();
        //Set the DatePicker minimum date selection to current date
        dp.setMinDate(cl.getTimeInMillis());//get the current day
        ta = TimePickerDialog(
            context, mTimeListener,
            mHour, mMin, false
        )
        sTime = sdfTime.format(cl1.time)
        //  txt_time.text = sTime
        //Registering button click listener
        btn_submit.setOnClickListener(this)
        txt_pickup.setOnClickListener(this)
        txt_dropoff.setOnClickListener(this)
        txt_date.setOnClickListener(this)
        txt_time.setOnClickListener(this)

        //Registering radio check change listener

        radioStroller.setOnCheckedChangeListener(this)
        radioCarseat.setOnCheckedChangeListener(this)
        radioBike.setOnCheckedChangeListener(this)
        radioSnowSki.setOnCheckedChangeListener(this)
        radioSurf.setOnCheckedChangeListener(this)
        radioPet.setOnCheckedChangeListener(this)
    }

    //for displaying picked date from calendar
    private fun updateDisplay() {
        txt_date.text = StringBuilder()
            // Month is 0 based so add 1
            .append(mYear).append("-")
            .append(String.format("%02d", mMonth)).append("-")
            .append(String.format("%02d", mDay))
    }

    //for displaying picked time from calendar
    private fun updateDisplayTime() {
        txt_time.text = StringBuilder()
            // Month is 0 based so add 1
            .append(mHour).append(":")
            .append(mMin)
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
         * @return A new instance of fragment AddRideFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): AddRideFragment {
            val fragment = AddRideFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    //Picking address from google
    private fun placeSearchRequest() {
        var intent: Intent? = null
        try {
            intent =
                PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(activity)
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }

        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var addrs: String? = null
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            val place = PlaceAutocomplete.getPlace(activity, data)
            Log.e("Place", place.toString())
            val name = place.name.toString()
            addrs = place.address.toString()
            Log.e("Address", "$name, $addrs")
            val lat = place.latLng.latitude
            val lng = place.latLng.longitude
            setAddress(addrs, lat, lng)
        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            val status = PlaceAutocomplete.getStatus(activity, data)
            // TODO: Handle the error.
            // TODO: Handle the error.
            Log.i("error", status.statusMessage.toString())

        } else if (resultCode == Activity.RESULT_CANCELED) {
            // The user canceled the operation.
            Log.i("error", "cancelled by user")

        }
    }

    //setting address on views
    private fun setAddress(addrs: String, lat: Double, lng: Double) {
        if (locaiton_type == 1) {
            txt_pickup.setText(addrs)
            pickup_lat = lat
            pickup_lng = lng
            pickUpCity =
                MapUtils.getCityNameByCoordinates(requireContext(), pickup_lat, pickup_lng, true)!!
            pickUpZip =
                MapUtils.getCityNameByCoordinates(requireContext(), pickup_lat, pickup_lng, false)!!
        } else if (locaiton_type == 2) {
            txt_dropoff.setText(addrs)
            drop_lat = lat
            drop_lng = lng
            dropCity =
                MapUtils.getCityNameByCoordinates(requireContext(), drop_lat, drop_lng, true)!!
            dropZip =
                MapUtils.getCityNameByCoordinates(requireContext(), drop_lat, drop_lng, false)!!
        }
    }

    //method for setting value for each checked
    private fun returnCheckValueforWholeForm(checkBox: RadioButton): String {
        var value: String = "1"
        if (checkBox.isChecked) {
            value = "1"
        } else {
            value = "0"
        }
        return value
    }

    //checking form validation
    @SuppressLint("NewApi")
    private fun checkFormValidation(): Boolean {
        var flag = true
        if (CommonMethods.isEmpty(txt_pickup)) {
            CommonMethods.showSnackBar(addParentt, context, getString(R.string.pickup_validation))
            flag = false
        } else if (CommonMethods.isEmpty(txt_dropoff)) {
            CommonMethods.showSnackBar(addParentt, context, getString(R.string.dropoff_validation))
            flag = false
        } else if (CommonMethods.isEmpty(vehicleType_edittext)) {
            CommonMethods.showSnackBar(
                addParentt,
                context,
                getString(R.string.vehicle_type_validation)
            )
            flag = false
        } else if (CommonMethods.isEmpty(txt_make)) {
            CommonMethods.showSnackBar(addParentt, context, getString(R.string.car_make_validation))
            flag = false
        }
        /*else if (CommonMethods.isEmpty(vehicleYear_edittext)) {
            CommonMethods.showSnackBar(addParentt, context, getString(R.string.vehicle_year_validation))
            flag = false
        }*/
        else if (spin_vehicle_year.selectedItem.equals(null)) {
            CommonMethods.showSnackBar(
                addParentt,
                context,
                getString(R.string.vehicle_year_validation)
            )
            flag = false
        } else if (CommonMethods.isEmpty(colorvehicle_edittext)) {
            CommonMethods.showSnackBar(
                addParentt,
                context,
                getString(R.string.color_field_validation)
            )
            flag = false
        }
        /* else if (howmanyDoorsSpinner.selectedItem.equals(getString(R.string.select_riders))) {
             CommonMethods.showSnackBar(addParentt, context, getString(R.string.doors_validation))
             flag = false
         }*/
        else if (CommonMethods.isEmpty(txt_date)) {
            CommonMethods.showSnackBar(addParentt, context, getString(R.string.date_validation))
            flag = false
        } else if (CommonMethods.isEmpty(txt_time)) {
            CommonMethods.showSnackBar(addParentt, context, getString(R.string.time_validation))
            flag = false
        } else if (txt_date.text.toString().equals(CommonMethods.getCurrentDateOnly())) {
            if (CommonMethods.getTime(txt_time.text.toString())
                    .compareTo(CommonMethods.getTime(CommonMethods.getCurrentTime12Format())) < 0
            ) {
                CommonMethods.showSnackBar(
                    addParentt,
                    context,
                    getString(R.string.valid_time_validation)
                )
                flag = false
            }
        } else if (CommonMethods.isEmpty(howmuchtogive_edittext)) {
            CommonMethods.showSnackBar(addParentt, context, getString(R.string.cost_validation))
            flag = false
        } else if (spin_rider.selectedItem.equals(getString(R.string.select_riders))) {
            CommonMethods.showSnackBar(addParentt, context, getString(R.string.riders_validation))
            flag = false
        }

        /*  else if(CommonMethods.isEmpty(riders_edittext)){
              CommonMethods.showSnackBar(addParentt,context,getString(R.string.riders_validation))
              flag = false
          }*/
        else if (CommonMethods.isEmpty(licenceplateno_edittext)) {
            CommonMethods.showSnackBar(addParentt, context, "Licence plate no can't be blank.")
            flag = false
        }

        /*else if (YesStroller.isChecked && CommonMethods.isEmpty(numb_of_stroller_edittext)){
            CommonMethods.showSnackBar(addParentt,context,getString(R.string.strollers_count_validation))
            numb_of_stroller_edittext.requestFocus()
            flag = false
        }else if (Yescar.isChecked && CommonMethods.isEmpty(numb_of_seat_edittext)){
            CommonMethods.showSnackBar(addParentt,context,getString(R.string.car_seat_validation))
            numb_of_seat_edittext.requestFocus()
            flag = false
        }else if (Yesbike.isChecked && CommonMethods.isEmpty(numb_of_bike_edittext)){
            CommonMethods.showSnackBar(addParentt,context,getString(R.string.bike_count_validation))
            numb_of_bike_edittext.requestFocus()
            flag = false
        }else if (Yesski.isChecked && CommonMethods.isEmpty(numb_of_ski_edittext)){
            CommonMethods.showSnackBar(addParentt,context,getString(R.string.snowboard_count_validation))
            numb_of_ski_edittext.requestFocus()
            flag = false
        }else if (Yessurf.isChecked && CommonMethods.isEmpty(numb_of_surf_edittext)){
            CommonMethods.showSnackBar(addParentt,context,getString(R.string.surfbaord_count_validation))
            numb_of_surf_edittext.requestFocus()
            flag = false
        }else if (Yespet.isChecked && CommonMethods.isEmpty(numb_of_pet_edittext)){
            CommonMethods.showSnackBar(addParentt,context,getString(R.string.pets_count_validation))
            numb_of_pet_edittext.requestFocus()
            flag = false
        }*/
        return flag
    }

    //interface method of click
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_submit -> {
                if (checkFormValidation()) {
                    CommonUtil.showProgressBar(rl_progress_bar)
                    BackgroundExecutor.instance.execute(
                        AddRoadTripRequester(
                            AddRoadTripRequest(
                                txt_pickup.text.toString(),
                                txt_dropoff.text.toString(),
                                vehicleType_edittext.text.toString(),
                                txt_make.text.toString(),
                                selected_year.toString(),
                                colorvehicle_edittext.text.toString(),
                                howmanyDoorsSpinner.selectedItem.toString(),
                                txt_date.text.toString(),
                                txt_time.text.toString(),
                                howmuchtogive_edittext.text.toString(),
                                spin_rider.selectedItem.toString(),
                                returnCheckValueforWholeForm(YesStroller),
                                returnCheckValueforWholeForm(Yescar),
                                returnCheckValueforWholeForm(Yesbike),
                                returnCheckValueforWholeForm(Yesski),
                                returnCheckValueforWholeForm(Yessurf),
                                returnCheckValueforWholeForm(Yespet),
                                "1",
                                numb_of_stroller_edittext.text.toString(),
                                numb_of_seat_edittext.text.toString(),
                                numb_of_bike_edittext.text.toString(),
                                numb_of_ski_edittext.text.toString(),
                                numb_of_surf_edittext.text.toString(),
                                numb_of_pet_edittext.text.toString(),
                                "" + pickup_lat,
                                "" + pickup_lng,
                                "" + drop_lat,
                                "" + drop_lng,
                                pickUpZip,
                                pickUpCity,
                                dropZip,
                                dropCity,
                                licenceplateno_edittext.text.toString()
                            )
                        )
                    )
                }
            }
            R.id.txt_date -> {
                da.show()
            }
            R.id.txt_time -> {
                ta.show()
            }
            R.id.txt_pickup -> {
                placeSearchRequest()
                locaiton_type = 1
            }
            R.id.txt_dropoff -> {
                placeSearchRequest()
                locaiton_type = 2
            }
        }
    }

    //interface method for checkbox
    override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
        when (p0?.id) {
            R.id.radioStroller -> {
                checkRadioButtonisOn(NoStroller, numb_of_stroller_edittext)
            }
            R.id.radioCarseat -> {
                checkRadioButtonisOn(Nocarr, numb_of_seat_edittext)
            }
            R.id.radioBike -> {
                checkRadioButtonisOn(Nobike, numb_of_bike_edittext)
            }
            R.id.radioSnowSki -> {
                checkRadioButtonisOn(Noski, numb_of_ski_edittext)
            }
            R.id.radioSurf -> {
                checkRadioButtonisOn(Nosurf, numb_of_surf_edittext)
            }
            R.id.radioPet -> {
                checkRadioButtonisOn(Nopet, numb_of_pet_edittext)
            }
        }
    }

    var res: Car_details? = null;

    //Eventbus method
    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        if (eventObject == null) {
            return
        }
        requireActivity().runOnUiThread(Runnable {
            try {
                onHandleBaseEvent(eventObject)
                CommonUtil.hideProgressBar(rl_progress_bar)
                val response = eventObject.`object` as BaseResponse
                when (eventObject.id) {

                    EventConstant.CARDETAILS_SUCCESS -> {
                        res = response.response_data!!.car_details!!
                        hitMake()
                    }
                    EventConstant.MAKE_SUCCESS -> {
                        vehicle_make = response.response_data!!.vehicle_make!!
                        vehicle_type = response.response_data!!.vehicle_type!!

                        val makevehicle: ArrayList<String> = ArrayList()
                        for (i in vehicle_make.indices)
                            makevehicle.add(vehicle_make[i].vehicle_make_title)

                        val riders = ArrayAdapter<String>(
                            requireActivity(),
                            R.layout.spinner_bigtext_items,
                            R.id.textvw,
                            makevehicle
                        );
                        spin_make?.adapter = riders

                        if (res != null) {

                            spin_make?.setSelection(makevehicle.indexOf(res!!.vehicle_make_title))
                        }

                        val vehictype: ArrayList<String> = ArrayList()
                        for (i in vehicle_type.indices)
                            vehictype.add(vehicle_type[i].type_name)
                        val vehtypeadapter = ArrayAdapter<String>(
                            requireActivity(),
                            R.layout.spinner_bigtext_items,
                            R.id.textvw,
                            vehictype
                        );
                        spin_vehicle_type?.adapter = vehtypeadapter

                        if (res != null) {
                            spin_vehicle_type?.setSelection(vehictype.indexOf(res!!.vehicle_type))

                        }
                        spin_vehicle_type.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    p0: AdapterView<*>?,
                                    p1: View?,
                                    p2: Int,
                                    p3: Long
                                ) {
                                    selected_vehicle = p2;
                                }

                                override fun onNothingSelected(p0: AdapterView<*>?) {
                                }

                            }

                        val door = ArrayAdapter<String>(
                            requireActivity(),
                            R.layout.spinner_bigtext_items,
                            R.id.textvw,
                            resources.getStringArray(R.array.doors)
                        );
                        spin_door?.adapter = door
                        if (res != null && !res?.no_of_door!!.equals("")) {
                            spin_door.setSelection(
                                resources.getStringArray(R.array.doors).indexOf(res?.no_of_door)
                            )

                        }

                        hitModel(spin_make.selectedItem.toString())
                        spin_make.onItemSelectedListener =
                            (object : AdapterView.OnItemSelectedListener {
                                override fun onNothingSelected(p0: AdapterView<*>?) {
                                }

                                override fun onItemSelected(
                                    p0: AdapterView<*>?,
                                    p1: View?,
                                    p2: Int,
                                    p3: Long
                                ) {
                                    selected_vehicle_makeid =
                                        vehicle_make[p2].vehicle_make_id.toInt()
                                    hitModel(vehicle_make[p2].vehicle_make_id)
                                }

                            })
                        fillDetails()
                    }
                    EventConstant.MODEL_SUCCESS -> {
                        vehicle_model = response.response_data!!.vehicle_model!!
                        val model: ArrayList<String> = ArrayList()
                        for (i in vehicle_model.indices)
                            model.add(vehicle_model[i].vehicle_model_title)


                        val riders = ArrayAdapter<String>(
                            requireActivity(),
                            R.layout.spinner_bigtext_items,
                            R.id.textvw,
                            model
                        );
                        spin_model?.adapter = riders

                        if (res != null && !res?.vehicle_model_id!!.equals("")) {

                            for (i in vehicle_model.indices) {
                                val auction = vehicle_model.get(i)
                                if (res?.vehicle_model_id!!.equals(auction.vehicle_model_id)) {
                                    spin_model.setSelection(i)

                                }
                            }

                        }
                        spin_model.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onNothingSelected(p0: AdapterView<*>?) {

                                }

                                override fun onItemSelected(
                                    p0: AdapterView<*>?,
                                    p1: View?,
                                    p2: Int,
                                    p3: Long
                                ) {
                                    selected_vehicle_modelid =
                                        vehicle_model[p2].vehicle_model_id.toInt()
                                }
                            }

                    }
                    EventConstant.REQUESTFORM_SUCCESS -> {
                        CommonMethods.showShortToast(context, response.response_msg)
                        FragmentFactory.removedBack(context)
                    }
                    EventConstant.REQUESTFORM_ERROR -> {
                        DialogUtil.showOkButtonDialog(
                            context,
                            getString(R.string.message),
                            response.response_msg,
                            response.response_key
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
    }

    fun hitMake() {
        CommonUtil.showProgressBar(rl_progress_bar)
        BackgroundExecutor.instance.execute(MakeRequester(BaseRequest(false, true, false, false)))
    }

    fun hitModel(vehicle_make_id: String) {
        CommonUtil.showProgressBar(rl_progress_bar)
        BackgroundExecutor.instance.execute(ModelRequester(ModelRequest(vehicle_make_id)))
    }

    //Enabling and disabling radio button according to radio buttons
    private fun checkRadioButtonisOn(radioButton: RadioButton, editText: EditText) {
        if (radioButton.isChecked) {
            editText.setEnabled(false)
        } else {
            editText.setEnabled(true)
            editText.requestFocus()
        }
    }

    fun hitCarDetailsApi() {
        CommonUtil.showProgressBar(rl_progress_bar)
        BackgroundExecutor.instance.execute(
            CarDetailsRequester(
                BaseRequest(
                    false,
                    true,
                    false,
                    false
                )
            )
        )
    }


}// Required empty public constructor
