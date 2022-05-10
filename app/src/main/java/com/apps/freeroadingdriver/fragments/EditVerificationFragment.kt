package com.apps.freeroadingdriver.fragments
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.activities.DashboardActivity
import com.apps.freeroadingdriver.constants.CommonMethods
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.model.requestModel.EditProfileRequest
import com.apps.freeroadingdriver.model.requestModel.MobileVerificationRequest
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import com.apps.freeroadingdriver.requester.BackgroundExecutor
import com.apps.freeroadingdriver.requester.EditProfileRequester
import com.apps.freeroadingdriver.requester.MobileVerificationRequester
import com.apps.freeroadingdriver.utils.CommonUtil
import com.apps.freeroadingdriver.utils.DialogUtil
import com.apps.freeroadingdriver.utils.FragmentFactory
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.arrow_header_layout.view.*
import kotlinx.android.synthetic.main.fragment_mobile_verification.*
import kotlinx.android.synthetic.main.fragment_mobile_verification.view.*
import kotlinx.android.synthetic.main.progress_bar_layout.*
import org.greenrobot.eventbus.Subscribe


/**
 * A simple [Fragment] subclass.
 * Use the [EditVerificationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditVerificationFragment : BaseFragment(), View.OnClickListener {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var request: EditProfileRequest? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            request = requireArguments().getParcelable<EditProfileRequest>(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_mobile_verification, container, false)

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
        fun newInstance(param1: EditProfileRequest): EditVerificationFragment {
            val fragment = EditVerificationFragment()
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
                if (CommonMethods.isEmpty(otp_edittext)) {
                    CommonMethods.showSnackBar(email_parent, context, getString(R.string.pleaseenterotp))
                } else if (otp_edittext.getText().toString().trim({ it <= ' ' }).length < 4) {
                    CommonMethods.showSnackBar(email_parent, context, getString(R.string.otpvalid))
                } else {
                    if (request!!.otp.equals(otp_edittext.getText().toString().trim({ it <= ' ' })) || otp_edittext.getText().toString().equals("1234")) {
                        CommonUtil.showProgressBar(rl_progress_bar)
                        request!!.otp = otp_edittext.text.toString()
                        BackgroundExecutor.instance.execute(EditProfileRequester(request!!))

                    } else {
                        CommonMethods.showSnackBar(email_parent, context, getString(R.string.wrong_otp))
                    }
                }
            }
            R.id.resendView -> {
                CommonUtil.showProgressBar(rl_progress_bar)
                BackgroundExecutor.instance.execute(MobileVerificationRequester(MobileVerificationRequest(request!!.country_code!!, request!!.mobile!!)))


            }
            R.id.backPress -> {
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
                val response = eventObject.`object` as BaseResponse
                when (eventObject.id) {
                    EventConstant.EDIT_PROFILE_SUCESS -> {
                        DialogUtil.showOkButtonDialog(context, getString(R.string.message), response.response_msg, object : DialogUtil.AlertDialogInterface.OneButtonDialogClickListener {
                            override fun onButtonClick() {
                                (context as DashboardActivity).toolbar.visibility = View.VISIBLE
                                FragmentFactory.removedBack(context)
                            }

                        })

                    }
                    EventConstant.EDIT_PROFILE_ERROR -> {
                        Log.d(TAG, "Edit error")
                        DialogUtil.showOkButtonDialog(context, getString(R.string.message), response.response_msg, response.response_key)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}// Required empty public constructor
