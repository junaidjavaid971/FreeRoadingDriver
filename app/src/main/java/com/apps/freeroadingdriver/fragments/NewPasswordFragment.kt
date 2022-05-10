package com.apps.freeroadingdriver.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.activities.LoginActivity
import com.apps.freeroadingdriver.constants.CommonMethods
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.model.requestModel.ResetPasswordRequest
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import com.apps.freeroadingdriver.requester.BackgroundExecutor
import com.apps.freeroadingdriver.requester.ResetPasswordRequester
import com.apps.freeroadingdriver.utils.CommonUtil
import com.apps.freeroadingdriver.utils.DialogUtil
import com.apps.freeroadingdriver.utils.FragmentFactory

import kotlinx.android.synthetic.main.arrow_header_layout.view.*
import kotlinx.android.synthetic.main.fragment_new_password.*
import kotlinx.android.synthetic.main.fragment_new_password.view.*
import kotlinx.android.synthetic.main.progress_bar_layout.*
import org.greenrobot.eventbus.Subscribe


/**
 * A simple [Fragment] subclass.
 * Use the [NewPasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewPasswordFragment : BaseFragment() {

    // TODO: Rename and change types of parameters
    private var custoerId: String? = null
    private var otp: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            custoerId = requireArguments().getString(ARG_PARAM1)
            otp = requireArguments().getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view : View = inflater!!.inflate(R.layout.fragment_new_password, container, false)
        view.backPress.setOnClickListener(View.OnClickListener {
            FragmentFactory.removedBack(context)
        })

        view.submitbutton.setOnClickListener(View.OnClickListener {
            if (CommonMethods.isEmpty(password_edittext)) {
                CommonMethods.showSnackBar(choosepParent, context, getString(R.string.enter_password))
            } else if (password_edittext.getText().toString().length < 8) {
                CommonMethods.showSnackBar(choosepParent, context, getString(R.string.valid_password_lenght))
            } else if (CommonMethods.isEmpty(confirmpassword_edittext)) {
                CommonMethods.showSnackBar(choosepParent, context, getString(R.string.confirmenter_password))
            } else if (password_edittext.getText().toString().trim({ it <= ' ' }) != confirmpassword_edittext.getText().toString().trim({ it <= ' ' })) {
                CommonMethods.showSnackBar(choosepParent, context, getString(R.string.txt_paswword_dosnt_matched))
            } else {
                CommonUtil.showProgressBar(rl_progress_bar)
                BackgroundExecutor.instance.execute(ResetPasswordRequester(ResetPasswordRequest(confirmpassword_edittext.text.toString(),
                        password_edittext.getText().toString(), custoerId!!,otp!!)))
            }
        })
        return view
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
         * @return A new instance of fragment NewPasswordFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): NewPasswordFragment {
            val fragment = NewPasswordFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    @Subscribe override fun onEvent(eventObject: EventObject) {
        Log.d(TAG, "onEvent method called")
        if (eventObject == null) {
            return
        }
        requireActivity().runOnUiThread {
            CommonUtil.hideProgressBar(rl_progress_bar)
            onHandleBaseEvent(eventObject)
            val response = eventObject.`object` as BaseResponse
            when (eventObject.id) {
                EventConstant.RESET_PASSWORD_SUCCESS -> if (context is LoginActivity) {
                    CommonMethods.showLongToast(context, response.response_msg)
                    startActivity(LoginActivity.createIntent(requireActivity()))
                }
                EventConstant.RESET_PASSWORD_ERROR -> {
                    Log.d(TAG, "forgot error")
                    DialogUtil.showOkButtonDialog(context, getString(R.string.message), response.response_msg, response.response_key)
                }
            }
        }
    }

}// Required empty public constructor
