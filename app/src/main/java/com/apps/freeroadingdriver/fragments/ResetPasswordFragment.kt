package com.apps.freeroadingdriver.fragments


import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.constants.CommonMethods
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.model.requestModel.ChangePassword
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import com.apps.freeroadingdriver.requester.BackgroundExecutor
import com.apps.freeroadingdriver.requester.ChangePasswordRequester
import com.apps.freeroadingdriver.utils.CommonUtil
import com.apps.freeroadingdriver.utils.DialogUtil
import com.apps.freeroadingdriver.utils.FragmentFactory
import kotlinx.android.synthetic.main.fragment_reset_password.*
import kotlinx.android.synthetic.main.fragment_reset_password.view.*
import kotlinx.android.synthetic.main.progress_bar_layout.*
import org.greenrobot.eventbus.Subscribe


/**
 * A simple [Fragment] subclass.
 * Use the [ResetPasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResetPasswordFragment : BaseFragment(),View.OnClickListener {

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_reset_password, container, false)
//        view.title.setText(getString(R.string.reset_pass))
            view.btn_reset.setOnClickListener(this)
        return view
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        private val TAG = ResetPasswordFragment::class.java.name

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ResetPasswordFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): ResetPasswordFragment {
            val fragment = ResetPasswordFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

    @Subscribe override fun onEvent(eventObject: EventObject) {
        if (eventObject == null) {
            return
        }
        requireActivity().runOnUiThread {
            try {
                CommonUtil.hideProgressBar(rl_progress_bar)
                onHandleBaseEvent(eventObject)
                val response = eventObject.`object` as BaseResponse
                when (eventObject.id) {
                    EventConstant.CHANGE_PASSWORD_SUCCESS -> {
                        Log.d(TAG, "change success")
                        CommonMethods.showShortToast(context, response.response_msg)
                        edt_old_password.setText("")
                        edt_new_password.setText("")
                        edt_confirm_password.setText("")
                        edt_old_password.requestFocus()
                    }
                    EventConstant.CHANGE_PASSWORD_ERROR -> {
                        Log.d(TAG, "change error")
                        DialogUtil.showOkButtonDialog(context, getString(R.string.message), response.response_msg, response.response_key)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.btn_reset->{
                CommonMethods.hideKeyboard(activity)
                if (isValidForm()) {
                    CommonUtil.showProgressBar(rl_progress_bar)
                    BackgroundExecutor.instance.execute(ChangePasswordRequester(ChangePassword(
                            edt_new_password.getText().toString(),  edt_old_password.getText().toString()
                    )))
                }
            }
        }
    }

    private fun isValidForm(): Boolean {
        var flag = true
        if (TextUtils.isEmpty(CommonMethods.getText(edt_old_password))) {
            CommonMethods.showSnackBar(parent, context, getString(R.string.old_pass_empty))
            flag = false
        } else if (TextUtils.isEmpty(CommonMethods.getText(edt_new_password))) {
            CommonMethods.showSnackBar(parent, context, getString(R.string.new_password_empty))
            flag = false
        } else if (TextUtils.isEmpty(CommonMethods.getText(edt_confirm_password))) {
            CommonMethods.showSnackBar(parent, context, getString(R.string.confirm_pass_empty))
            flag = false
        } else if (CommonMethods.getText(edt_new_password).length < 8) {
            CommonMethods.showSnackBar(parent, context, getString(R.string.password_length))
            flag = false
        } else if (!CommonMethods.getText(edt_new_password).equals(CommonMethods.getText(edt_confirm_password))) {
            CommonMethods.showSnackBar(parent, context, getString(R.string.passswords_dontmatch))
            flag = false
        }
        return flag
    }
}// Required empty public constructor
