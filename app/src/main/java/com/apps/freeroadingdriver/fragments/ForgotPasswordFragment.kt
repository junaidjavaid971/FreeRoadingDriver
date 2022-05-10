package com.apps.freeroadingdriver.fragments


import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.activities.BaseActivity
import com.apps.freeroadingdriver.activities.LoginActivity
import com.apps.freeroadingdriver.constants.CommonMethods
import com.apps.freeroadingdriver.dialogs.CountryPicker
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.interfaces.CountryPickerListener
import com.apps.freeroadingdriver.model.requestModel.MobileVerificationRequest
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import com.apps.freeroadingdriver.requester.BackgroundExecutor
import com.apps.freeroadingdriver.requester.ForgotPasswordRequester
import com.apps.freeroadingdriver.utils.CommonUtil
import com.apps.freeroadingdriver.utils.FragmentFactory
import kotlinx.android.synthetic.main.arrow_header_layout.view.*
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import kotlinx.android.synthetic.main.fragment_forgot_password.view.*
import kotlinx.android.synthetic.main.progress_bar_layout.*
import org.greenrobot.eventbus.Subscribe


/**
 * A simple [Fragment] subclass.
 * Use the [ForgotPasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ForgotPasswordFragment : BaseFragment(), View.OnClickListener, CountryPickerListener {

    override fun onSelectCountry(name: String?, code: String?, dialCode: String?) {
        tv_cc.text = dialCode
        stdcode = dialCode
    }

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var stdcode: String? = null

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
        val v: View = inflater!!.inflate(R.layout.fragment_forgot_password, container, false)
        v.ll_country_code.setOnClickListener(this)
        v.resetButton.setOnClickListener(this)
        v.backPress.setOnClickListener(this)
        return v
    }

    companion object {
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        private val TAG = ForgotPasswordFragment::class.java.name


        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ForgotPasswordFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): ForgotPasswordFragment {
            val fragment = ForgotPasswordFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.ll_country_code -> {
                val dialog = CountryPicker.newInstance("Select Country")
                dialog.setListener(this)
                dialog.show(requireActivity().supportFragmentManager, "dialog")
            }
            R.id.resetButton -> {
                if (TextUtils.isEmpty(stdcode)) {
                    CommonMethods.showSnackBar(
                        forgot_parent,
                        context,
                        getString(R.string.country_code_validation)
                    )
                } else if (CommonMethods.isEmpty(edt_mobile_number)) {
                    CommonMethods.showSnackBar(
                        forgot_parent,
                        context,
                        getString(R.string.empty_mobile_number)
                    )
                } else if (edt_mobile_number.getText().toString()
                        .trim { it <= ' ' }.length < 6 || edt_mobile_number.getText().toString()
                        .trim({ it <= ' ' }).length > 14
                ) {
                    CommonMethods.showSnackBar(
                        forgot_parent,
                        context,
                        requireActivity().getString(R.string.valid_mobile_number)
                    )
                } else {
                    CommonUtil.showProgressBar(rl_progress_bar)
                    BackgroundExecutor.instance.execute(
                        ForgotPasswordRequester(
                            MobileVerificationRequest(
                                stdcode!!,
                                edt_mobile_number.getText().toString()
                            )
                        )
                    )
                }
            }
            R.id.backPress -> {
                FragmentFactory.removedBack(context)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        tv_cc.text = stdcode
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
                        CommonMethods.showLongToast(context, response.response_msg)
                        replaceFragment(
                            OTPFragment.newInstance(
                                response.response_data!!.otp!!,
                                response.response_data!!.driver_id!!,
                                stdcode!!,
                                edt_mobile_number.text.toString()
                            ), TAG
                        )
                    }
                    EventConstant.FORGOT_PASSWORD_ERROR -> {
                        Log.d(TAG, "forgot error")
                        replaceFragment(WrongEmailFragment(), TAG)
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
