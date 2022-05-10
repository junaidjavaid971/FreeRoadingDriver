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
import android.widget.ArrayAdapter
import com.craterzone.media.images.CameraUtil
import com.craterzone.media.images.ImageUtil
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.activities.DashboardActivity
import com.apps.freeroadingdriver.constants.AppConstant
import com.apps.freeroadingdriver.constants.AppConstant.TEMP_PHOTO_FILE_NAME
import com.apps.freeroadingdriver.constants.CommonMethods
import com.apps.freeroadingdriver.dialogs.CountryPicker
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.fragments.SignupFragment.Companion.REQUEST_CHECK_SETTINGS
import com.apps.freeroadingdriver.interfaces.CountryPickerListener
import com.apps.freeroadingdriver.manager.UserManager
import com.apps.freeroadingdriver.model.dataModel.Profile
import com.apps.freeroadingdriver.model.requestModel.BaseRequest
import com.apps.freeroadingdriver.model.requestModel.EditProfileRequest
import com.apps.freeroadingdriver.model.requestModel.MobileVerificationRequest
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import com.apps.freeroadingdriver.model.responseModel.Interest
import com.apps.freeroadingdriver.permissions.PermissionRequest
import com.apps.freeroadingdriver.permissions.PermissionRequestHandler
import com.apps.freeroadingdriver.permissions.PermissionsUtil
import com.apps.freeroadingdriver.prefrences.FreeRoadingPreferenceManager
import com.apps.freeroadingdriver.requester.BackgroundExecutor
import com.apps.freeroadingdriver.requester.EditProfileRequester
import com.apps.freeroadingdriver.requester.MobileVerificationRequester
import com.apps.freeroadingdriver.requester.ProfileRequester
import com.apps.freeroadingdriver.utils.*
import com.cz.imagelib.Crop
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_add_ride.*
import kotlinx.android.synthetic.main.fragment_car_details.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.progress_bar_layout.*
import org.greenrobot.eventbus.Subscribe
import java.io.File
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : BaseFragment(), PermissionRequest.RequestCameraStoragePermissionGroup,
    PermissionRequest.RequestStorage, CountryPickerListener, View.OnClickListener {
    override fun onSelectCountry(name: String?, code: String?, dialCode: String?) {
        tv_cc.setText(dialCode)
    }

    override fun onStoragePermissionGranted() {
        Crop.pickImage(activity)
    }

    override fun onStoragePermissionDenied() {
    }

    override fun onAllCameraStoragePermissionGroupGranted() {
        Crop.captureImage(activity)
    }

    override fun onCameraStoragePermissionGroupDenied() {
    }

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mFileTemp: File? = null
    private var imageUri: Uri? = null
    private var licenseUri: Uri? = null
    private var isProfileImage: Boolean? = false
    private var userProfile: Profile? = null
    var interest: Array<Interest>? = null
    internal var cl = Calendar.getInstance()
    internal var mYear = cl.get(Calendar.YEAR)
    internal var mMonth = cl.get(Calendar.MONTH)
    internal var mDay = cl.get(Calendar.DAY_OF_MONTH)
    internal lateinit var daExp: DatePickerDialog
    internal lateinit var daIssue: DatePickerDialog


    private val mDateExpSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            mYear = year
            mMonth = monthOfYear + 1
            mDay = dayOfMonth
            updateExpDate()
        }
    private val mDateIssueSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            mYear = year
            mMonth = monthOfYear + 1
            mDay = dayOfMonth
            updateIssueDate()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_profile_new, container, false)
        userProfile = UserManager.getInstance().getUser()
        fetchProfile()

        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {
            mFileTemp = File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE_NAME)
        } else {
            mFileTemp = File(requireActivity().filesDir, TEMP_PHOTO_FILE_NAME)
        }
        registerClickEvent(view)



        daExp = DatePickerDialog(
            requireActivity(), android.R.style.Theme_Holo_Dialog, mDateExpSetListener,
            mYear, mMonth, mDay
        )



        daIssue = DatePickerDialog(
            requireActivity(), android.R.style.Theme_Holo_Dialog, mDateIssueSetListener,
            mYear, mMonth, mDay
        )


        return view
    }

    private fun updateExpDate() {
        txt_exp_date.text = StringBuilder()
            // Month is 0 based so add 1
            .append(mYear).append("-")
            .append(String.format("%02d", mMonth)).append("-")
            .append(String.format("%02d", mDay))
    }

    private fun updateIssueDate() {
        txt_issue_date.text = StringBuilder()
            // Month is 0 based so add 1
            .append(mYear).append("-")
            .append(String.format("%02d", mMonth)).append("-")
            .append(String.format("%02d", mDay))
    }

    private fun fetchProfile() {
        CommonUtil.showProgressBar(rl_progress_bar)
        BackgroundExecutor.instance.execute(
            ProfileRequester(
                BaseRequest(
                    false,
                    true,
                    false,
                    false
                )
            )
        )
    }

    private fun registerClickEvent(view: View) {
        view.ll_country_code1.setOnClickListener(this)
        view.profilePic1.setOnClickListener(this)
        view.iv_driver_license.setOnClickListener(this)
        view.save.setOnClickListener(this)
        view.txt_exp_date.setOnClickListener(this)
        view.txt_issue_date.setOnClickListener(this)
    }

    private fun setDefaultView() {
        if (userProfile != null) {
            try {
                val classes = ArrayAdapter<String>(
                    requireActivity(),
                    R.layout.spinner_bigtext_items,
                    R.id.textvw,
                    resources.getStringArray(R.array.classes)
                );
                spin_class?.adapter = classes

                val music_pref = ArrayAdapter<String>(
                    requireActivity(),
                    R.layout.spinner_bigtext_items,
                    R.id.textvw,
                    resources.getStringArray(R.array.music_pref)
                );
                spin_music_pref?.adapter = music_pref


                var inters: ArrayList<String> = ArrayList()
                for (item: Interest in interest!!) {
                    inters.add(item.name)
                }

                val interestAdapter = ArrayAdapter<String>(
                    requireActivity(),
                    R.layout.spinner_bigtext_items,
                    R.id.textvw,
                    inters
                );
                spin_interest.adapter = interestAdapter;
                if (userProfile!!.music_preferences.equals("1")) {
                    rg_music.check(R.id.music_yes)
                } else {
                    rg_music.check(R.id.music_no)
                }
                if (userProfile!!.pet_friendly.equals("1")) {
                    rg_pet.check(R.id.pet_yes)
                } else {
                    rg_pet.check(R.id.pet_no)
                }

                if (userProfile!!.smoking.equals("1")) {
                    rg_smoke.check(R.id.smoking_yes)

                } else {
                    rg_smoke.check(R.id.smoking_no)
                }

                if (userProfile!!.wheel_drive_type.equals("2")) {
                    rg_wheel_drive.check(R.id.radio_2_wheel)

                } else if (userProfile!!.wheel_drive_type.equals("4")) {
                    rg_wheel_drive.check(R.id.radio_4_wheel)
                } else {
                    rg_wheel_drive.check(R.id.radio_all_wheel)

                }

                if (userProfile!!.class_license_held.equals(
                        resources.getStringArray(R.array.classes).get(1)
                    )
                ) {
                    spin_class.setSelection(1)
                } else if (userProfile!!.class_license_held.equals(
                        resources.getStringArray(R.array.classes).get(2)
                    )
                ) {
                    spin_class.setSelection(2)
                } else if (userProfile!!.class_license_held.equals(
                        resources.getStringArray(R.array.classes).get(3)
                    )
                ) {
                    spin_class.setSelection(3)
                } else if (userProfile!!.class_license_held.equals(
                        resources.getStringArray(R.array.classes).get(4)
                    )
                ) {
                    spin_class.setSelection(4)
                } else {
                    spin_class.setSelection(0)
                }
                /* var  musicList=resources.getStringArray(R.array.music_pref);
                 for (item  in musicList.indices){
                     if (userProfile!!.music_preferences.equals(musicList[item])){
                         spin_music_pref.setSelection(item)
                     }
                 }*/
                name_edittext.setText(userProfile!!.name)
                edt_mobile_number.setText("" + userProfile!!.mobile)
                email_edittext.setText(userProfile!!.email)
                tv_cc.setText(userProfile!!.country_code)
                edt_licence_nn.setText(userProfile!!.driver_license_no)
                edt_expdate.setText(userProfile!!.license_expiry_date)
                edt_address.setText(userProfile!!.home_addrs)
                edt_license_experience.setText(userProfile!!.driving_experience)
                edt_hobies.setText(userProfile!!.hobbies)
                edt_insta_account.setText(userProfile!!.instagram_url)
                edt_interest.setText(userProfile!!.interests)
                edt_lang_spoke.setText(userProfile!!.languages_spoken)
                edt_linkedin_account.setText(userProfile!!.linkedin_url)
                edt_fb_account.setText(userProfile!!.facebook_url)


                edt_driver_license_no.setText(userProfile!!.driver_license_no)
                txt_exp_date.setText(userProfile!!.license_expiry_date)
                txt_issue_date.setText(userProfile!!.license_issue_date)
                edt_state_issue.setText(userProfile!!.state_issue)

                GlideUtil.loadImageWithDefaultImage(
                    requireActivity(),
                    iv_driver_license,
                    CommonMethods.getDriverImageUrl(userProfile!!.driver_license_pic),
                    R.drawable.ic_profile_placeholder
                )
                GlideUtil.loadImageWithDefaultImage(
                    requireActivity(),
                    profilePic1,
                    CommonMethods.getDriverImageUrl(userProfile!!.profile_pic),
                    R.drawable.ic_profile_placeholder
                )
            } catch (e: Exception) {
            }

        }
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
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): ProfileFragment {
            val fragment = ProfileFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.d(TAG, "onRequestPermissionResult()")
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
            FileUtil.copyImageFile(requireActivity(), imageCaptureUri, mFileTemp!!.getPath())
        UCrop.of(imageCaptureUri, Uri.fromFile(copyImagePath))
            .start(requireActivity())
    }

    private fun handleCrop(resultCode: Int, result: Intent) {
        if (resultCode == Activity.RESULT_OK) {

            if (isProfileImage!!) {
                imageUri = UCrop.getOutput(result)
                GlideUtil.loadImageCirculer(requireActivity(), profilePic1, imageUri)
            } else {
                licenseUri = UCrop.getOutput(result)
                GlideUtil.loadImageCirculer(requireActivity(), iv_driver_license, licenseUri)
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
                Log.d(TAG, "onActivityResult Request pick")
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(TAG, "onActivityResult Request pick success")
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
                Log.d(TAG, "onActivityResult Request capture")
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(TAG, "onActivityResult request capture success")
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
                Log.d(TAG, "onActivityResult Request crop success")
                try {
                    handleCrop(resultCode, result!!)
                } catch (ex: Exception) {
                }

            }
            REQUEST_CHECK_SETTINGS -> when (resultCode) {
                Activity.RESULT_OK -> {
                }
                Activity.RESULT_CANCELED -> {
                }
                else -> {
                }
            }
        }
    }

    var isMusic: String? = "0";
    var isSmoking: String? = "0";
    var isPet: String? = "0";
    var wheel_driver: String? = "";

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.ll_country_code1 -> {
                val dialog = CountryPicker.newInstance("Select Country")
                dialog.setListener(this)
                dialog.show((context as FragmentActivity).supportFragmentManager, "dialog")
            }
            R.id.profilePic1 -> {
                isProfileImage = true
                Log.d(TAG, "on profile pic clicked")
                DialogUtil.openChooseMediaDialog(
                    requireActivity(),
                    object : DialogUtil.AlertDialogInterface.OpenCameraDialogListener {
                        override fun onCameraClick() {
                            Log.d(TAG, "on Camera click")
                            PermissionRequestHandler.requestCameraStoragePermissionGroup(
                                this@ProfileFragment,
                                "",
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA
                            )
                        }

                        override fun onGalleryClick() {
                            Log.d(TAG, "on Gallary click")
                            PermissionRequestHandler.requestStorage(this@ProfileFragment, "")
                        }
                    })
            }
            R.id.iv_driver_license -> {
                isProfileImage = false
                Log.d(TAG, "on profile pic clicked")
                DialogUtil.openChooseMediaDialog(
                    requireActivity(),
                    object : DialogUtil.AlertDialogInterface.OpenCameraDialogListener {
                        override fun onCameraClick() {
                            Log.d(TAG, "on Camera click")
                            PermissionRequestHandler.requestCameraStoragePermissionGroup(
                                this@ProfileFragment,
                                "",
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA
                            )
                        }

                        override fun onGalleryClick() {
                            Log.d(TAG, "on Gallary click")
                            PermissionRequestHandler.requestStorage(this@ProfileFragment, "")
                        }
                    })
            }
            R.id.txt_exp_date -> {
                daExp.show()
            }
            R.id.txt_issue_date -> {
                daIssue.show()
            }


            R.id.save -> {

                if (rg_music.checkedRadioButtonId == R.id.music_yes) {
                    isMusic = ""
                } else {
                    isMusic = "01"
                }

                if (rg_pet.checkedRadioButtonId == R.id.pet_yes) {
                    isPet = "1";
                } else {
                    isPet = "0"
                }

                if (rg_smoke.checkedRadioButtonId == R.id.smoking_yes) {
                    isSmoking = "1"
                } else {
                    isSmoking = "0"
                }
                if (rg_wheel_drive.checkedRadioButtonId == R.id.radio_2_wheel) {
                    wheel_driver = "2"
                } else if (rg_wheel_drive.checkedRadioButtonId == R.id.radio_4_wheel) {
                    wheel_driver = "4"
                } else {
                    wheel_driver = "all"
                }

                if (imageUri == null && userProfile!!.profile_pic.equals("")) {
                    CommonMethods.showSnackBar(
                        profile_parent,
                        requireActivity(),
                        "Please upload your profile picture."
                    )
                } else if (CommonMethods.isEmpty(name_edittext)) {
                    CommonMethods.showSnackBar(
                        profile_parent,
                        requireActivity(),
                        getString(R.string.name_valid)
                    )
                } else if (CommonMethods.isEmpty(edt_mobile_number)) {
                    CommonMethods.showSnackBar(
                        profile_parent,
                        requireActivity(),
                        getString(R.string.empty_mobile_number)
                    )
                } else if (edt_mobile_number.getText().toString()
                        .trim({ it <= ' ' }).length < 6 || edt_mobile_number.getText().toString()
                        .trim({ it <= ' ' }).length > 14
                ) {
                    CommonMethods.showSnackBar(
                        profile_parent,
                        requireActivity(),
                        requireActivity().getString(R.string.valid_mobile_number)
                    )
                }
//                else if (imageUri == null && userProfile!!.profile_pic.equals("")) {
//                    CommonMethods.showSnackBar(profile_parent, requireActivity(), "Please upload your profile picture.")
//                }
                else if (CommonMethods.isEmpty(edt_driver_license_no)) {
                    CommonMethods.showSnackBar(
                        profile_parent,
                        requireActivity(),
                        requireActivity().getString(R.string.please_enter_license_no)
                    )
                } else if (CommonMethods.isEmpty(txt_exp_date)) {
                    CommonMethods.showSnackBar(
                        profile_parent,
                        requireActivity(),
                        requireActivity().getString(R.string.please_enter_exp_date)
                    )
                } else if (CommonMethods.isEmpty(txt_issue_date)) {
                    CommonMethods.showSnackBar(
                        profile_parent,
                        requireActivity(),
                        requireActivity().getString(R.string.please_enter_issue_date)
                    )
                } else if (CommonMethods.isEmpty(edt_state_issue)) {
                    CommonMethods.showSnackBar(
                        profile_parent,
                        requireActivity(),
                        requireActivity().getString(R.string.please_enter_state_of_issue)
                    )
                }
                /* else if(CommonMethods.isEmpty(edt_hobies)){
                     CommonMethods.showSnackBar(profile_parent, requireActivity(), "Please enter hobbies.")
                 }*/
                else {
                    if (!("" + userProfile!!.mobile).equals(edt_mobile_number.text.toString())) {
                        CommonUtil.showProgressBar(rl_progress_bar)
                        BackgroundExecutor.instance.execute(
                            MobileVerificationRequester(
                                MobileVerificationRequest(
                                    tv_cc.text.toString(),
                                    edt_mobile_number.text.toString()
                                )
                            )
                        )
                    } else {
                        hitEditApi("")
                    }
                }
            }
        }
    }

    private fun hitEditApi(otp: String) {
        try {


            CommonUtil.showProgressBar(rl_progress_bar)
            BackgroundExecutor.instance.execute(
                EditProfileRequester(
                    EditProfileRequest(
                        name_edittext.text.toString(),
                        email_edittext.text.toString(),
                        edt_mobile_number.text.toString(),
                        imageUri?.path,
                        tv_cc.text.toString(),
                        otp,
                        edt_address.text.toString(),
                        spin_class.selectedItem.toString(),
                        edt_license_experience.text.toString(),
                        edt_fb_account.text.toString(),
                        edt_hobies.text.toString(),
                        edt_insta_account.text.toString(),
                        spin_interest.selectedItem.toString(),
                        edt_lang_spoke.text.toString(),
                        edt_linkedin_account.text.toString(),
                        spin_music_pref.selectedItem.toString(),
                        isPet,
                        isSmoking,
                        licenseUri?.path,
                        wheel_driver,
                        edt_driver_license_no.text.toString(),
                        txt_exp_date.text.toString(),
                        txt_issue_date.text.toString(),
                        edt_state_issue.text.toString()
                    )
                )
            )
        } catch (e: Exception) {
            CommonUtil.hideProgressBar(rl_progress_bar)
            e.printStackTrace()
            //edt_driver_license_no.text.toString(), txt_exp_date.text.toString(),txt_issue_date.text.toString(),edt_state_issue.text.toString()
        }

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
                val response = eventObject.`object` as BaseResponse
                when (eventObject.id) {
                    EventConstant.MOBILE_VERIFICATION_SUCCESS -> {
                        Log.d(TAG, "" + response.response_data!!)
                        var editProfileRequest: EditProfileRequest = EditProfileRequest(
                            name_edittext.text.toString(),
                            email_edittext.text.toString(),
                            edt_mobile_number.text.toString(),
                            imageUri?.path,
                            tv_cc.text.toString(),
                            response.response_data!!.otp,
                            edt_address.text.toString(),
                            edt_class_license.text.toString(),
                            edt_license_experience.text.toString(),
                            edt_fb_account.text.toString(),
                            edt_hobies.text.toString(),
                            edt_insta_account.text.toString(),
                            spin_interest.selectedItem.toString(),
                            edt_lang_spoke.text.toString(),
                            edt_linkedin_account.text.toString(),
                            isMusic,
                            isPet,
                            isSmoking,
                            licenseUri?.path,
                            wheel_driver,
                            edt_driver_license_no.text.toString(),
                            txt_exp_date.text.toString(),
                            txt_issue_date.text.toString(),
                            edt_state_issue.text.toString()
                        )
                        changeFragment(EditVerificationFragment.newInstance(editProfileRequest!!))
                        // hitEditApi(final_otp!!)
                    }
                    EventConstant.PROFILE_SUCCESS -> {
                        interest = response.response_data!!.interest;
                        setDefaultView()
                    }
                    EventConstant.EDIT_PROFILE_SUCESS -> {
                        CommonMethods.showShortToast(requireActivity(), response.response_msg);
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
                        //startActivity(DashboardActivity.createIntent(requireActivity()))
                    }
                    EventConstant.EDIT_PROFILE_ERROR, EventConstant.MOBILE_VERIFICATION_ERROR -> {
                        Log.d(TAG, "edit error");
                        DialogUtil.showOkButtonDialog(
                            requireActivity(),
                            getString(R.string.message),
                            response.response_msg,
                            response.response_key
                        );
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun changeFragment(fragment: Fragment) {
        FragmentFactory.replaceFragmentWithAnim(
            fragment,
            R.id.fragment_container,
            requireActivity(),
            TAG
        )
        if (context is DashboardActivity) {
            (context as DashboardActivity).setEnableDisableDrawer(false)
            (context as DashboardActivity).setBackVisibility(true)
            (context as DashboardActivity).toolbar.visibility = View.GONE
        }
    }
}// Required empty public constructor
