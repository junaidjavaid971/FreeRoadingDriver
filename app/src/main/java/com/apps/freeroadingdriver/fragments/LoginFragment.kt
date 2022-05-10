package com.apps.freeroadingdriver.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.activities.HomeActivity
import com.apps.freeroadingdriver.activities.LoginActivity
import com.apps.freeroadingdriver.activities.SignUpActivity
import com.apps.freeroadingdriver.constants.CommonMethods
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.model.requestModel.LoginRequest
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import com.apps.freeroadingdriver.prefrences.FreeRoadingPreferenceManager
import com.apps.freeroadingdriver.requester.BackgroundExecutor
import com.apps.freeroadingdriver.requester.LoginUserRequester
import com.apps.freeroadingdriver.utils.CommonUtil
import com.apps.freeroadingdriver.utils.DialogUtil
import com.apps.freeroadingdriver.utils.FragmentFactory
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.progress_bar_layout.*
import org.greenrobot.eventbus.Subscribe

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : BaseFragment(), View.OnClickListener {

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
        val view: View = inflater!!.inflate(R.layout.fragment_login, container, false)
        view.forgotpassword.setOnClickListener(this)
        view.signupHolder.setOnClickListener(this)
        view.loginbutton.setOnClickListener(this)
        return view
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        val TAG = LoginFragment::class.java.simpleName

        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): LoginFragment {
            val fragment = LoginFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.forgotpassword -> {
                FragmentFactory.replaceFragmentWithAnim(
                    ForgotPasswordFragment(),
                    R.id.fragment_container,
                    context,
                    TAG
                )
            }
            R.id.signupHolder -> {
                startActivity(SignUpActivity.createIntent(context!!))
            }
            R.id.loginbutton -> {
                if (CommonMethods.isEmpty(email_edittext)) {
                    CommonMethods.showSnackBar(
                        login_parent,
                        context,
                        context!!.getString(R.string.empty_email_mobile)
                    )
                } else if (CommonMethods.isEmpty(password_edittext)) {
                    CommonMethods.showSnackBar(
                        login_parent,
                        context,
                        context!!.getString(R.string.enter_password)
                    )
                } else {
                    CommonUtil.showProgressBar(rl_progress_bar)
                    BackgroundExecutor.instance.execute(
                        LoginUserRequester(
                            LoginRequest(
                                password_edittext.text.toString(),
                                email_edittext.text.toString()
                            )
                        )
                    )
                }

            }
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
                    EventConstant.LOGIN_SUCCESS -> if (context is LoginActivity) {
//                        if (response.getResponse_data().getVehilce_type() != null)
//                            CommonMethods.showLongToast(context, response.response_msg.getVehilce_type().getMsg())
                        CommonMethods.showLongToast(context, response.response_msg)

                        if (response.response_data?.profile?.stripe_connect_status.equals("1"))
                            FreeRoadingPreferenceManager.getInstance().stripeToken = "1"
                        else
                            FreeRoadingPreferenceManager.getInstance().stripeToken = "0"
                        startActivity(Intent(context, HomeActivity::class.java))
                        requireActivity().finish()
                    }
                    EventConstant.LOGIN_ERROR -> {
                        Log.d(TAG, "login error")
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
        }
    }

}// Required empty public constructor
