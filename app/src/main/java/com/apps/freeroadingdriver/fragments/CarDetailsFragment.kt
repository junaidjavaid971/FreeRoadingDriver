package com.apps.freeroadingdriver.fragments

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.activities.DashboardActivity
import com.apps.freeroadingdriver.constants.AppConstant
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.model.requestModel.BaseRequest
import com.apps.freeroadingdriver.model.requestModel.ModelRequest
import com.apps.freeroadingdriver.model.requestModel.SaveVehicleRequest
import com.apps.freeroadingdriver.model.responseModel.*
import com.apps.freeroadingdriver.permissions.PermissionRequest
import com.apps.freeroadingdriver.permissions.PermissionRequestHandler
import com.apps.freeroadingdriver.permissions.PermissionsUtil
import com.apps.freeroadingdriver.prefrences.FreeRoadingPreferenceManager
import com.apps.freeroadingdriver.requester.*
import com.apps.freeroadingdriver.utils.CommonUtil
import com.apps.freeroadingdriver.utils.DialogUtil
import com.apps.freeroadingdriver.utils.FileUtil
import com.apps.freeroadingdriver.utils.GlideUtil
import com.craterzone.media.images.CameraUtil
import com.craterzone.media.images.ImageUtil
import com.cz.imagelib.Crop
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.fragment_car_details.*
import kotlinx.android.synthetic.main.fragment_car_details.view.*
import kotlinx.android.synthetic.main.progress_bar_layout.*
import org.greenrobot.eventbus.Subscribe
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [CarDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CarDetailsFragment : BaseFragment(), PermissionRequest.RequestCameraStoragePermissionGroup,
    PermissionRequest.RequestStorage {
    internal var cl = Calendar.getInstance()
    internal lateinit var da: DatePickerDialog
    internal var mYear = cl.get(Calendar.YEAR)
    internal var mMonth = cl.get(Calendar.MONTH)
    internal var mDay = cl.get(Calendar.DAY_OF_MONTH)
    private var sDate: String? = null
    private var selected_year: String? = ""
    internal var sdf = SimpleDateFormat("yyyy-MM-dd")
    override fun onStoragePermissionGranted() {
        Log.d(BaseFragment.TAG, "Storage permission granted")
        Crop.pickImage(activity)
    }

    override fun onStoragePermissionDenied() {
        Log.d(BaseFragment.TAG, "Storage permission denied")
    }

    override fun onAllCameraStoragePermissionGroupGranted() {
        Log.d(BaseFragment.TAG, "Camera permission granted")
        Crop.captureImage(activity)
    }

    override fun onCameraStoragePermissionGroupDenied() {
        Log.d(BaseFragment.TAG, "Camera permission denied")
    }

    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mFileTemp: File? = null
    private var image_front_Uri: Uri? = null
    private var image_back_Uri: Uri? = null
    private var image_insurence_Uri: Uri? = null
    private var isFrontImage: Boolean = false
    private var isBackImage: Boolean = false
    private var isinsuranceImage: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }

    private val mDateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            mYear = year
            mMonth = monthOfYear + 1
            mDay = dayOfMonth
            updateDisplay()
        }

    private fun updateDisplay() {
        tv_manfecture_year.text = StringBuilder()
            // Month is 0 based so add 1
            .append(mYear).append("-")
            .append(mMonth).append("-")
            .append(mDay)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_car_details, container, false)
        val list_years: java.util.ArrayList<String> = java.util.ArrayList()
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
        view.spin_year?.adapter = v_years
        view.spin_year?.setSelection(0)

        view.spin_year.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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


        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {
            mFileTemp =
                File(Environment.getExternalStorageDirectory(), AppConstant.TEMP_PHOTO_FILE_NAME)
        } else {
            mFileTemp = File(requireActivity().filesDir, AppConstant.TEMP_PHOTO_FILE_NAME)
        }

        view.iv_vehicle_front.setOnClickListener {
            isFrontImage = true
            isBackImage = false
            isinsuranceImage = false
            DialogUtil.openChooseMediaDialog(
                requireActivity(),
                object : DialogUtil.AlertDialogInterface.OpenCameraDialogListener {
                    override fun onCameraClick() {
                        PermissionRequestHandler.requestCameraStoragePermissionGroup(
                            this@CarDetailsFragment,
                            "",
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                        )
                    }

                    override fun onGalleryClick() {
                        Log.d(BaseFragment.TAG, "on Gallary click")
                        PermissionRequestHandler.requestStorage(this@CarDetailsFragment, "")
                    }
                })
        }

        da = DatePickerDialog(
            requireActivity(), mDateSetListener,
            mYear, mMonth, mDay
        )
        da.getDatePicker().setMaxDate(System.currentTimeMillis());


        sDate = sdf.format(cl.time)
        view.tv_manfecture_year.setOnClickListener {
            da.show()

        }

        view.btn_submit.setOnClickListener {
            var saveVehicleRequest: SaveVehicleRequest = SaveVehicleRequest()
            if (rg_ac.checkedRadioButtonId == R.id.ac_yes) {
                saveVehicleRequest.is_ac = "1"
            } else {
                saveVehicleRequest.is_ac = "0"
            }

            if (rg_type_of_vehicle.checkedRadioButtonId == R.id.radio_hybrid) {
                saveVehicleRequest.is_hybrid_electric = "1"
            } else if (rg_type_of_vehicle.checkedRadioButtonId == R.id.radio_electric) {
                saveVehicleRequest.is_hybrid_electric = "2"
            } else if (rg_type_of_vehicle.checkedRadioButtonId == R.id.radio_diesel) {
                saveVehicleRequest.is_hybrid_electric = "3"
            } else {
                saveVehicleRequest.is_hybrid_electric = "4"
            }

            if (rg_wifi.checkedRadioButtonId == R.id.wifi_yes) {
                saveVehicleRequest.vehicle_wifi = "1"
            } else {
                saveVehicleRequest.vehicle_wifi = "0"
            }
            saveVehicleRequest.license_plate_no = view.edt_licence.text.toString()
            saveVehicleRequest.no_of_door = view.spin_door.selectedItem.toString()
            saveVehicleRequest.no_of_seat = view.spin_seat.selectedItem.toString()
            saveVehicleRequest.state = view.ed_state.text.toString()
            saveVehicleRequest.taxi_back_view = image_back_Uri?.path
            saveVehicleRequest.taxi_front_view = image_front_Uri?.path
            saveVehicleRequest.upload_motor_insurance_cert = image_insurence_Uri?.path
            //saveVehicleRequest.vehicle_year = view.tv_manfecture_year.text.toString()
            saveVehicleRequest.vehicle_year = selected_year
            saveVehicleRequest.road_tax_no = ""
            saveVehicleRequest.upload_cert_reg_exp_date = ""
            saveVehicleRequest.vehicle_color = view.ed_vehicle_color.text.toString()
            saveVehicleRequest.vehicle_insurance_no = ""
            saveVehicleRequest.vehicle_make_id = selected_vehicle_makeid.toString()
            saveVehicleRequest.vehicle_model_id = selected_vehicle_modelid.toString()
            saveVehicleRequest.vehicle_reg_no = view.ed_reg_number.text.toString();
            // saveVehicleRequest.vehicle_year = view.tv_manfecture_year.text.toString()
            saveVehicleRequest.vehicle_type_id =
                vehicle_type.get(selected_vehicle!!).vehicle_type_id
            saveVehicleRequest.upload_cert_reg_exp_date = ""
            saveVehicleRequest.liability_coverage_amount =
                view.ed_liability_coverage_amount.text.toString()
            saveVehicleRequest.collision_coverage_amount =
                view.ed_collision_coverage_amount.text.toString()
            saveVehicleRequest.comprehensive_coverage_amount =
                view.ed_comprehensive_coverage_cmount.text.toString()
            saveVehicleRequest.uninsured_motorist_coverage_amount =
                view.ed_underinsured_motorist_coverage_amount.text.toString()
            saveVehicleRequest.personal_injury_coverage_amount =
                view.ed_personal_injury_coverage_amount.text.toString()
            CommonUtil.showProgressBar(rl_progress_bar)
            BackgroundExecutor.instance.execute(VehicleAddRequester(saveVehicleRequest))
        }
        fun validate(): Boolean {

            return true;
        }
        view.iv_vehicle_back.setOnClickListener {
            isFrontImage = false
            isBackImage = true
            isinsuranceImage = false
            DialogUtil.openChooseMediaDialog(
                requireActivity(),
                object : DialogUtil.AlertDialogInterface.OpenCameraDialogListener {
                    override fun onCameraClick() {
                        Log.d(BaseFragment.TAG, "on Camera click")
                        PermissionRequestHandler.requestCameraStoragePermissionGroup(
                            this@CarDetailsFragment,
                            "",
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                        )
                    }

                    override fun onGalleryClick() {
                        Log.d(BaseFragment.TAG, "on Gallary click")
                        PermissionRequestHandler.requestStorage(this@CarDetailsFragment, "")
                    }
                })
        }


        view.iv_insurence_certificae.setOnClickListener {
            isFrontImage = false
            isBackImage = false
            isinsuranceImage = true
            DialogUtil.openChooseMediaDialog(
                requireActivity(),
                object : DialogUtil.AlertDialogInterface.OpenCameraDialogListener {
                    override fun onCameraClick() {
                        Log.d(BaseFragment.TAG, "on Camera click")
                        PermissionRequestHandler.requestCameraStoragePermissionGroup(
                            this@CarDetailsFragment,
                            "",
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                        )
                    }

                    override fun onGalleryClick() {
                        Log.d(BaseFragment.TAG, "on Gallary click")
                        PermissionRequestHandler.requestStorage(this@CarDetailsFragment, "")
                    }
                })
        }
        hitCarDetailsApi()
        return view
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        private val TAG = CarDetailsFragment::class.java.name

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CarDetailsFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): CarDetailsFragment {
            val fragment = CarDetailsFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
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


    fun hitMake() {
        CommonUtil.showProgressBar(rl_progress_bar)
        BackgroundExecutor.instance.execute(MakeRequester(BaseRequest(false, true, false, false)))
    }

    fun hitModel(vehicle_make_id: String) {
        CommonUtil.showProgressBar(rl_progress_bar)
        BackgroundExecutor.instance.execute(ModelRequester(ModelRequest(vehicle_make_id)))
    }

    var res: Car_details? = null;
    var selected_vehicle: Int? = null
    var selected_vehicle_makeid: Int? = null
    var selected_vehicle_modelid: Int? = null
    lateinit var vehicle_make: Array<Vehicle_make>;
    lateinit var vehicle_type: Array<Vehicle_type>;
    lateinit var vehicle_model: Array<Vehicle_model>;


    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        Log.d(TAG, "OnEvent called")
        if (eventObject == null) {
            return
        }
        requireActivity().runOnUiThread(Runnable {
            try {
                CommonUtil.hideProgressBar(rl_progress_bar)
                onHandleBaseEvent(eventObject)
                val response = eventObject.`object` as BaseResponse
                when (eventObject.id) {
                    EventConstant.CARDETAILS_SUCCESS -> {
                        res = response.response_data!!.car_details!!
                        hitMake();
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

                            if (res!!.is_ac.equals("1")) {
                                rg_ac.check(R.id.ac_yes)

                            } else {
                                rg_ac.check(R.id.ac_no)
                            }

                            if (res!!.is_hybrid_electric.equals("1")) {
                                rg_type_of_vehicle.check(R.id.radio_hybrid)

                            } else if (res!!.is_hybrid_electric.equals("2")) {
                                rg_type_of_vehicle.check(R.id.radio_electric)
                            } else if (res!!.is_hybrid_electric.equals("3")) {
                                rg_type_of_vehicle.check(R.id.radio_diesel)
                            } else {
                                rg_type_of_vehicle.check(R.id.radio_gas)

                            }

                            if (res!!.vehicle_wifi.equals("1")) {
                                rg_wifi.check(R.id.wifi_yes)

                            } else {
                                rg_wifi.check(R.id.wifi_no)
                            }
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
                        val seat = ArrayAdapter<String>(
                            requireActivity(),
                            R.layout.spinner_bigtext_items,
                            R.id.textvw,
                            resources.getStringArray(R.array.seat)
                        );
                        spin_seat?.adapter = seat
                        if (res != null && !res?.no_of_seat!!.equals("")) {
                            spin_seat.setSelection(
                                resources.getStringArray(R.array.seat).indexOf(res?.no_of_seat)
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
                        showData()
                    }

//
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

                            for (i in 0 until vehicle_model.size) {
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

                    EventConstant.VEHICLE_SUCCESS -> {
                        //      FragmentFactory.removedBack(context)
                        requireActivity().finish()
                        if (FreeRoadingPreferenceManager.getInstance().rideRoadType) {
                            startActivity(
                                DashboardActivity.createIntent(requireActivity())
                                    .putExtra(AppConstant.IS_ROAD_TRIP, true)
                            )
                        } else {
                            startActivity(
                                DashboardActivity.createIntent(requireActivity())
                                    .putExtra(AppConstant.IS_ROAD_TRIP, false)
                            )
                        }


                    }
                    EventConstant.VEHICLE_FAILURE -> {
                        DialogUtil.showOkButtonDialog(
                            requireActivity(),
                            getString(R.string.message),
                            response.response_msg,
                            response.response_key
                        )
                    }
                    EventConstant.CARDETAILS_ERROR -> {
                        Log.d(TAG, "statistics error")
                        hitMake();
                        //  DialogUtil.showOkButtonDialog(requireActivity(), getString(R.string.message), response.response_msg, response.response_key)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
    }

    fun showData() {
        tv_manfecture_year.setText(res?.vehicle_year)

        edt_licence.setText(res?.license_plate_no)
        ed_state.setText(res?.state)
        ed_reg_number.setText(res?.vehicle_reg_no)
        ed_vehicle_color.setText(res?.vehicle_color)
        ed_collision_coverage_amount.setText(res?.collision_coverage_amount)
        ed_comprehensive_coverage_cmount.setText(res?.comprehensive_coverage_amount)
        ed_liability_coverage_amount.setText(res?.liability_coverage_amount)
        ed_personal_injury_coverage_amount.setText(res?.personal_injury_coverage_amount)
        ed_underinsured_motorist_coverage_amount.setText(res?.uninsured_motorist_coverage_amount)
        GlideUtil.loadImageWithDefaultImage(
            requireActivity(),
            iv_vehicle_front,
            "http://freeroading.onsisdev.info/public/media/vehicle/" + res!!.taxi_front_view,
            R.drawable.ic_profile_placeholder
        )
        GlideUtil.loadImageWithDefaultImage(
            requireActivity(),
            iv_vehicle_back,
            "http://freeroading.onsisdev.info/public/media/vehicle/" + res!!.taxi_back_view,
            R.drawable.ic_profile_placeholder
        )
        GlideUtil.loadImageWithDefaultImage(
            requireActivity(),
            iv_insurence_certificae,
            "http://freeroading.onsisdev.info/public/media/vehicle/" + res!!.upload_motor_insurance_cert,
            R.drawable.ic_profile_placeholder
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.d(BaseFragment.TAG, "onRequestPermissionResult()")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionsUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun pickImageFromGallery(uri: Uri?) {
        var imagePath = ImageUtil.getPath(requireActivity(), uri)
        imagePath = CameraUtil.roateImageIfRequired(imagePath)
        val copyImageFile = FileUtil.copyImageFile(requireActivity(), uri, imagePath)
        if (uri != null) {
            UCrop.of(uri, Uri.fromFile(copyImageFile))
                .start(requireActivity())
        }
    }

    private fun captureImageFromCamera() {
        val imageCaptureUri = CameraUtil.getLastImageUri()
        //        Uri imageCaptureUri =  FileProvider.getUriForFile(requireActivity(), BuildConfig.APPLICATION_ID + ".provider", mFileTemp);
        //        String imagePath = ImageUtil.getPath(requireActivity(), imageCaptureUri);
        //        imagePath = CameraUtil.roateImageIfRequired(imagePath);
        val copyImagePath =
            FileUtil.copyImageFile(requireActivity(), imageCaptureUri, mFileTemp!!.path)
        UCrop.of(imageCaptureUri, Uri.fromFile(copyImagePath))
            .start(requireActivity())
    }

    private fun handleCrop(resultCode: Int, result: Intent) {
        if (resultCode == Activity.RESULT_OK) {
            if (isFrontImage) {
                image_front_Uri = UCrop.getOutput(result);
                GlideUtil.loadImageCirculer(requireActivity(), iv_vehicle_front, image_front_Uri)
            } else if (isBackImage) {
                image_back_Uri = UCrop.getOutput(result);
                GlideUtil.loadImageCirculer(requireActivity(), iv_vehicle_back, image_back_Uri)

            } else {
                image_insurence_Uri = UCrop.getOutput(result);
                GlideUtil.loadImageCirculer(
                    requireActivity(),
                    iv_insurence_certificae,
                    image_insurence_Uri
                )
            }

        } else if (resultCode == UCrop.RESULT_ERROR) {
            Toast.makeText(requireActivity(), UCrop.getError(result)!!.message, Toast.LENGTH_SHORT)
                .show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, result: Intent?) {
        super.onActivityResult(requestCode, resultCode, result)
        when (requestCode) {
            Crop.REQUEST_PICK -> {
                Log.d(BaseFragment.TAG, "onActivityResult Request pick")
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(BaseFragment.TAG, "onActivityResult Request pick success")
                    pickImageFromGallery(result?.data)
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(
                        requireActivity(),
                        "User cancelled image pick",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            Crop.REQUEST_CAPTURE -> {
                Log.d(BaseFragment.TAG, "onActivityResult Request capture")
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(BaseFragment.TAG, "onActivityResult request capture success")
                    /* if (result!=null && result.getData()!=null) {
                    captureImageFromCamera();
                    }else {
                        Bitmap bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
                        profilePic.setImageBitmap(bitmap);
                    }*/
                    captureImageFromCamera()
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // user cancelled Image capture
                } else {
                    // failed to capture image
                }
            }
            UCrop.REQUEST_CROP -> {
                Log.d(BaseFragment.TAG, "onActivityResult Request crop success")
                handleCrop(resultCode, result!!)
            }
            SignupFragment.REQUEST_CHECK_SETTINGS -> when (resultCode) {
                Activity.RESULT_OK -> {
                }
                Activity.RESULT_CANCELED -> {

                }
                else -> {
                }
            }
        }
    }


}// Required empty public constructor
