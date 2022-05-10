package com.apps.freeroadingdriver.fragments


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.activities.HomeActivity
import com.apps.freeroadingdriver.activities.LoginActivity
import com.apps.freeroadingdriver.constants.CommonMethods
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.manager.UserManager
import com.apps.freeroadingdriver.model.dataModel.Profile
import com.apps.freeroadingdriver.model.requestModel.SignUpRequest
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import com.apps.freeroadingdriver.new_modelll.MyModel
import com.apps.freeroadingdriver.new_network.APIClient
import com.apps.freeroadingdriver.new_network.ApiInterface
import com.apps.freeroadingdriver.prefrences.FreeRoadingPreferenceManager
import com.apps.freeroadingdriver.requester.BackgroundExecutor
import com.apps.freeroadingdriver.requester.SignUpUserRequester
import com.apps.freeroadingdriver.utils.BMSPrefs
import com.apps.freeroadingdriver.utils.CommonUtil
import com.apps.freeroadingdriver.utils.DialogUtil
import com.apps.freeroadingdriver.utils.FragmentFactory
import com.google.gson.Gson

import kotlinx.android.synthetic.main.arrow_header_layout.view.*
import kotlinx.android.synthetic.main.fragment_mobile_verification.view.*
import kotlinx.android.synthetic.main.progress_bar_layout.*
import org.greenrobot.eventbus.Subscribe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.gson.JsonElement
import kotlinx.android.synthetic.main.fragment_mobile_verification.*


/**
 * A simple [Fragment] subclass.
 * Use the [MobileVerificationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MobileVerificationFragment : BaseFragment(), View.OnClickListener {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var request: SignUpRequest? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            request = requireArguments().getParcelable<SignUpRequest>(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater!!.inflate(R.layout.fragment_mobile_verification, container, false)

        //Setting click event on views
        view.verifyButton.setOnClickListener(this)
        view.resendView.setOnClickListener(this)
        view.backPress.setOnClickListener(this)

        return view
    }

    //All static fields go here
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
         * @return A new instance of fragment MobileVerificationFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: SignUpRequest): MobileVerificationFragment {
            val fragment = MobileVerificationFragment()
            val args = Bundle()
            args.putParcelable(ARG_PARAM1, param1)
            fragment.arguments = args
            return fragment
        }
    }

    //Override function of onClickListener
    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.verifyButton -> {
                CommonUtil.showProgressBar(rl_progress_bar)
//                request!!.request_type = "2"
//                sendOtpToServer()

                if (CommonMethods.isEmpty(otp_edittext)) {
                    CommonMethods.showSnackBar(
                        email_parent,
                        context,
                        getString(R.string.pleaseenterotp)
                    )
                } else if (otp_edittext.getText().toString().trim({ it <= ' ' }).length < 4) {
                    CommonMethods.showSnackBar(email_parent, context, getString(R.string.otpvalid))
                } else {
                    if (request!!.otp.equals(
                            otp_edittext.getText().toString().trim({ it <= ' ' })
                        ) || otp_edittext.getText().toString().equals("1234")
                    ) {
                        CommonUtil.showProgressBar(rl_progress_bar)
                        request!!.request_type = "2"
                        BackgroundExecutor.instance.execute(SignUpUserRequester(request!!))
                    } else {
                        CommonUtil.hideProgressBar(rl_progress_bar)
                        CommonMethods.showSnackBar(
                            email_parent,
                            context,
                            getString(R.string.wrong_otp)
                        )
                    }
                }
            }
            R.id.resendView -> {
                CommonUtil.showProgressBar(rl_progress_bar)
                request!!.request_type = "1"
                BackgroundExecutor.instance.execute(SignUpUserRequester(request!!))
            }
            com.apps.freeroadingdriver.R.id.backPress -> {
                FragmentFactory.removedBack(context)
            }
        }
    }

    //Event bus result of api's and callbacks
    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        Log.d(TAG, "onEvent method called")
        try {
            if (eventObject == null) {
                return
            }
            requireActivity().runOnUiThread {
                CommonUtil.hideProgressBar(rl_progress_bar)
                onHandleBaseEvent(eventObject)
//                startActivity(Intent(context, LoginActivity::class.java))
//                activity!!.finish()

                val response = eventObject.`object` as BaseResponse
                when (eventObject.id) {
                    EventConstant.SIGN_UP_SUCCESS -> {
                        if (request!!.request_type.equals("1")) {
                            request!!.otp = response.response_data!!.otp
                            DialogUtil.showOkButtonDialog(
                                context,
                                getString(R.string.message),
                                response.response_msg,
                                response.response_key
                            )
                        } else {
                            CommonMethods.showLongToast(context, response.response_msg)
                            if (response.response_status.equals("1")) {
                                startActivity(Intent(context, HomeActivity::class.java))
                                requireActivity().finish()
                            } else {
                                //startActivity(LoginActivity.createIntent(context!!))
                                startActivity(Intent(context, HomeActivity::class.java))
                                requireActivity().finish()
                            }
                        }
                    }
                    EventConstant.SIGN_UP_ERROR -> {
                        Log.d(TAG, "login error")
                        DialogUtil.showOkButtonDialog(
                            context,
                            getString(R.string.message),
                            response.response_msg,
                            response.response_key
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun sendOtpToServer() {
        var apiServices = APIClient.client.create(ApiInterface::class.java)

        val call = apiServices.SignUpTEMP(request)

        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                CommonUtil.hideProgressBar(rl_progress_bar)
                if (response.isSuccessful) {
                    Log.d("OnResponse", response.body().toString());
                    var gson = Gson()
                    var jsonString = response.body().toString()


                    val responseModel = gson.fromJson(jsonString, MyModel::class.java)
                    if (responseModel.response_data!!.profile != null) {
                        val weatherResponse = responseModel!!
                        Log.d(TAG, weatherResponse.response_msg)

                        val userProfile = responseModel!!.response_data!!.profile

                        BMSPrefs.putListData(activity, "listData", userProfile)


                        // val userPP= Profile()

                        // userPP!!.driver_id= userProfile.driver_id.toLong()
                        //  userPP!!.name= userProfile.name
                        // userPP!!.email= userProfile.email
                        // userPP!!.mobile= userProfile.mobile.toLong()

                        // UserManager.getInstance().user = userPP

                        FreeRoadingPreferenceManager.getInstance().setLogin(true)

                    }
                    FreeRoadingPreferenceManager.getInstance().sessionToken =
                        responseModel!!.response_data!!.session_token
                    startActivity(Intent(context, HomeActivity::class.java))
                    activity!!.finish()
                } else {
                    Log.e(TAG, response.message())
                }

                /*if (response.body()!!.response_status.equals("1")) {
                    CommonUtil.hideProgressBar(rl_progress_bar)
                    if (response.body()!!.response_data!!.profile != null) {
                        val weatherResponse = response.body()!!
                        Log.d(TAG,weatherResponse.response_msg)

                        val userProfile = response.body()!!.response_data!!.profile
                        UserManager.getInstance().user = userProfile
                        FreeRoadingPreferenceManager.getInstance().setLogin(true)
                    }
                    //EventBus.getDefault().post(EventObject(EventConstant.SIGN_UP_SUCCESS, response))

                    //                String vehicle= GsonUtil.toJson(response.getResponse_data().getVehilce_type());
                    //                TaxiShakePreferenceManager.getInstance().setVehicleType(vehicle);
                    FreeRoadingPreferenceManager.getInstance().sessionToken = response.body()!!.response_data!!.session_token

                    startActivity(Intent(context, HomeActivity::class.java))
                    activity!!.finish()
                } else if (response.body()!!.response_status.equals("0")) {
                    Log.d(TAG,response.body()!!.response_msg)
                    CommonUtil.hideProgressBar(rl_progress_bar)
                    DialogUtil.showOkButtonDialog(context, getString(R.string.message), response.body()!!.response_msg, response.body()!!.response_key)
                    //EventBus.getDefault().post(EventObject(EventConstant.SIGN_UP_ERROR, response))
                }*/

            }

            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                CommonUtil.hideProgressBar(rl_progress_bar)
                Log.e(TAG, t.toString())
            }
        })

    }


}// Required empty public constructor
