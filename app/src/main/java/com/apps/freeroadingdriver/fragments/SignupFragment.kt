package com.apps.freeroadingdriver.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.ScrollView
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.activities.BaseActivity
import com.apps.freeroadingdriver.activities.LoginActivity
import com.apps.freeroadingdriver.activities.SignUpActivity
import com.apps.freeroadingdriver.constants.AppConstant
import com.apps.freeroadingdriver.constants.CommonMethods
import com.apps.freeroadingdriver.dialogs.CountryPicker
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.interfaces.CountryPickerListener
import com.apps.freeroadingdriver.model.requestModel.SignUpRequest
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import com.apps.freeroadingdriver.requester.BackgroundExecutor
import com.apps.freeroadingdriver.requester.SignUpUserRequester
import com.apps.freeroadingdriver.utils.CommonUtil
import com.apps.freeroadingdriver.utils.DialogUtil
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.android.synthetic.main.fragment_signup.view.*
import org.greenrobot.eventbus.Subscribe
/**
 * A simple [Fragment] subclass.
 * Use the [SignupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignupFragment : BaseFragment(), CountryPickerListener {
    override fun onSelectCountry(name: String?, code: String?, dialCode: String?) {
        tv_cc.text = dialCode
        stdcode = dialCode
        edt_mobile_number.requestFocus()
    }


    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var stdcode: String? = null
    private var request: SignUpRequest? = null
    private lateinit var signup_parent : ScrollView
    private lateinit var rl_progress_bar : RelativeLayout

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
        val view:View = inflater!!.inflate(R.layout.fragment_signup, container, false)
        signup_parent = view.findViewById(R.id.signup_parent)
        rl_progress_bar = view.findViewById(R.id.rl_progress_bar)


        view.ll_country_code.setOnClickListener(View.OnClickListener {
            val dialog = CountryPicker.newInstance("Select Country")
            dialog.setListener(this)
            dialog.show(requireActivity().supportFragmentManager, "dialog")
        })

        view.signupbutton.setOnClickListener(View.OnClickListener {
            if (isValidForm()){
                CommonUtil.showProgressBar(rl_progress_bar)
                setSignUpRequest()
                BackgroundExecutor.instance.execute(SignUpUserRequester(request!!))
            }
        })

        view.loginHolder.setOnClickListener(View.OnClickListener {
            startActivity(LoginActivity.createIntent(requireActivity()))
        })
        view.terms_condition.setOnClickListener(View.OnClickListener {
            (context as BaseActivity).changeFragment(TermsConditionFragment.newInstance(AppConstant.TERMS,true),TermsConditionFragment.TAG)
        })
        return view
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        val REQUEST_CHECK_SETTINGS = 500
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SignupFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): SignupFragment {
            val fragment = SignupFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onResume() {
        super.onResume()
        tv_cc.text = stdcode
    }

    @Subscribe override fun onEvent(eventObject: EventObject) {
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
                    EventConstant.SIGN_UP_SUCCESS -> {
                        request!!.otp = response.response_data!!.otp
                        if (context is SignUpActivity) {
                            (context as SignUpActivity).changeFragmentWithTag(MobileVerificationFragment.newInstance(request!!), TAG)
                        }
                    }
                    EventConstant.SIGN_UP_ERROR -> {
                        Log.d(TAG, "login error")
                        DialogUtil.showOkButtonDialog(context, getString(R.string.message), response.response_msg, response.response_key)
                    }
                }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
    }
    private fun setSignUpRequest() {
        request = SignUpRequest()
        request!!.name = name_edittext.text.toString()
        request!!.email = email_edittext.text.toString()
        request!!.mobile = edt_mobile_number.text.toString()
        request!!.password = password_edittext.text.toString()
        request!!.country_code = tv_cc.text.toString()
        request!!.confirm_password =confirmpassword_edittext.text.toString()
        request!!.request_type = "1"
    }

    fun isValidForm(): Boolean {
        if (CommonMethods.isEmpty(name_edittext)) {
            CommonMethods.showSnackBar(signup_parent, context, requireActivity().getString(R.string.name_valid))
            name_edittext.requestFocus()
            return false
        }else if(TextUtils.isEmpty(stdcode)){
            CommonMethods.showSnackBar(signup_parent,context,getString(R.string.country_code_validation))
            return false
        } else if (CommonMethods.isEmpty(edt_mobile_number)) {
            CommonMethods.showSnackBar(signup_parent, context, requireActivity().getString(R.string.empty_mobile_number))
            edt_mobile_number.requestFocus()
            return false
        } else if (edt_mobile_number.getText().toString().trim({ it <= ' ' }).length < 6 || edt_mobile_number.getText().toString().trim({ it <= ' ' }).length > 14) {
            CommonMethods.showSnackBar(signup_parent, context, requireActivity().getString(R.string.valid_mobile_number))
            edt_mobile_number.requestFocus()
            return false
        } else if (CommonMethods.isEmpty(email_edittext)) {
            CommonMethods.showSnackBar(signup_parent, context, requireActivity().getString(R.string.empty_email))
            email_edittext.requestFocus()
            return false
        } else if (!CommonMethods.validateEmail(email_edittext.getText().toString())) {
            CommonMethods.showSnackBar(signup_parent, context, requireActivity().getString(R.string.inavlid_email))
            email_edittext.requestFocus()
            return false
        } else if (CommonMethods.isEmpty(password_edittext)) {
            CommonMethods.showSnackBar(signup_parent, context, getString(R.string.enter_password))
            password_edittext.requestFocus()
            return false
        } else if (password_edittext.getText().toString().length < 8) {
            CommonMethods.showSnackBar(signup_parent, context, getString(R.string.valid_password_lenght))
            password_edittext.requestFocus()
            return false
        } else if (CommonMethods.isEmpty(confirmpassword_edittext)) {
            CommonMethods.showSnackBar(signup_parent, context, getString(R.string.confirmenter_password))
            confirmpassword_edittext.requestFocus()
            return false
        } else if (password_edittext.getText().toString().trim({ it <= ' ' }) != confirmpassword_edittext.getText().toString().trim({ it <= ' ' })) {
            CommonMethods.showSnackBar(signup_parent, context, getString(R.string.txt_paswword_dosnt_matched))
            confirmpassword_edittext.requestFocus()
            return false
        } else if (!terms_condition_check.isChecked()) {
            CommonMethods.showSnackBar(signup_parent, context, getString(R.string.aggree_terms))
            return false
        }
        return true
    }
}// Required empty public constructor
