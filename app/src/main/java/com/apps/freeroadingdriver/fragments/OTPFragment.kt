package com.apps.freeroadingdriver.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.activities.BaseActivity
import com.apps.freeroadingdriver.activities.LoginActivity
import com.apps.freeroadingdriver.constants.CommonMethods
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.model.requestModel.MobileVerificationRequest
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import com.apps.freeroadingdriver.requester.BackgroundExecutor
import com.apps.freeroadingdriver.requester.ForgotPasswordRequester
import com.apps.freeroadingdriver.utils.CommonUtil
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import kotlinx.android.synthetic.main.fragment_ot.*
import kotlinx.android.synthetic.main.fragment_ot.view.*
import kotlinx.android.synthetic.main.progress_bar_layout.*
import org.greenrobot.eventbus.Subscribe


/**
 * A simple [Fragment] subclass.
 * Use the [OTPFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OTPFragment : BaseFragment() {

    // TODO: Rename and change types of parameters
    private var otp: String? = null
    private var customerId: String? = null
    private var stdCode: String? = null
    private var phone: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            otp = requireArguments().getString(ARG_PARAM1)
            customerId = requireArguments().getString(ARG_PARAM2)
            stdCode = requireArguments().getString(ARG_PARAM3)
            phone = requireArguments().getString(ARG_PARAM4)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view : View = inflater!!.inflate(R.layout.fragment_ot, container, false)

        view.resendHolder.setOnClickListener(View.OnClickListener {
            CommonUtil.showProgressBar(rl_progress_bar)
            BackgroundExecutor.instance.execute(ForgotPasswordRequester(MobileVerificationRequest(stdCode!!,phone!!)))

        })


        view.verifyButton.setOnClickListener(View.OnClickListener {
            if (CommonMethods.isEmpty(otp_edittext)) {
                CommonMethods.showSnackBar(otp_parent, context, getString(R.string.pleaseenterotp))
            } else if (otp_edittext.getText().toString().trim({ it <= ' ' }).length < 4) {
                CommonMethods.showSnackBar(otp_parent, context, getString(R.string.otpvalid))
            } else {
                if (otp.equals(otp_edittext.getText().toString().trim({ it <= ' ' }))) {
                    replaceFragment(NewPasswordFragment.newInstance(customerId!!,otp!!), TAG)
                } else {
                    CommonMethods.showSnackBar(otp_parent, context, getString(R.string.wrong_otp))
                }
            }
        })
        return view
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        private val ARG_PARAM3 = "param3"
        private val ARG_PARAM4 = "param4"
        private val TAG = OTPFragment::class.java.name

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OTPFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String, param3 : String, param4 : String): OTPFragment {
            val fragment = OTPFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            args.putString(ARG_PARAM3, param3)
            args.putString(ARG_PARAM4, param4)
            fragment.arguments = args
            return fragment
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
                    EventConstant.FORGOT_PASSWORD_SUCCESS -> if (context is LoginActivity) {
                        CommonMethods.showLongToast(context,response.response_msg)
                        otp = response.response_data!!.otp
                    }
                    EventConstant.FORGOT_PASSWORD_ERROR -> {
                        Log.d(TAG, "forgot error")
                        CommonMethods.showLongToast(context,response.response_msg)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun replaceFragment(fragment: BaseFragment, TAG: String) {
        if (context is BaseActivity) {
            (context as BaseActivity).changeFragmentWithTag(fragment, TAG)
        }
    }
}// Required empty public constructor
