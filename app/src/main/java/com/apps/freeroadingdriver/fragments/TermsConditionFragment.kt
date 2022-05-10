package com.apps.freeroadingdriver.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.constants.AppConstant
import com.apps.freeroadingdriver.constants.CommonMethods
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.model.requestModel.TermsAboutRequest
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import com.apps.freeroadingdriver.requester.BackgroundExecutor
import com.apps.freeroadingdriver.requester.TermsAndAboutRequester
import com.apps.freeroadingdriver.utils.CommonUtil
import com.apps.freeroadingdriver.utils.DialogUtil
import com.apps.freeroadingdriver.utils.FragmentFactory
import kotlinx.android.synthetic.main.arrow_black_header_layout.view.*
import kotlinx.android.synthetic.main.fragment_terms_condition.*
import kotlinx.android.synthetic.main.fragment_terms_condition.view.*
import kotlinx.android.synthetic.main.progress_bar_layout.*
import org.greenrobot.eventbus.Subscribe


/**
 * A simple [Fragment] subclass.
 * Use the [TermsConditionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TermsConditionFragment : BaseFragment() {
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
                    EventConstant.PAGES_SUCESS -> {
                        val page_desc = CommonMethods.returnNotnullString(response.response_data!!.page!!.pages_desc)
                        terms_condition_view.setText(android.text.Html.fromHtml(page_desc))
                    }
                    EventConstant.PAGE_ERROR -> {
                        Log.d(TAG, "page error")
                        DialogUtil.showOkButtonDialog(context, getString(R.string.message), response.response_msg, response.response_key)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var isFromSignup: Boolean = false
    private var isTermsorAbout: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            isFromSignup = requireArguments().getBoolean(IS_FROM_SIGNUP)

            if (mParam1.equals(AppConstant.TERMS, ignoreCase = true)) {
                isTermsorAbout = 1
            } else if (mParam1.equals(AppConstant.CONTACT, ignoreCase = true)) {
                isTermsorAbout = 2
            } else if (mParam1.equals(AppConstant.HELP, ignoreCase = true)) {
                isTermsorAbout = 3
            } else if (mParam1.equals(AppConstant.SUPPORT, ignoreCase = true)) {
                isTermsorAbout = 4
            } else if (mParam1.equals(AppConstant.PRIVACY, ignoreCase = true)) {
                isTermsorAbout = 5
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_terms_condition, container, false)
        if(isFromSignup){
            view.title.text = requireActivity().getString(R.string.tnc)
            view.backPressd.setOnClickListener(View.OnClickListener {
                FragmentFactory.removedBack(context)
            })
        }else{
            view.blackHeader.visibility = View.GONE
        }
        hitApi(isTermsorAbout)
        return view
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val IS_FROM_SIGNUP = "param2"
        val TAG = TermsConditionFragment::class.java.name

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TermsConditionFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: Boolean): TermsConditionFragment {
            val fragment = TermsConditionFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putBoolean(IS_FROM_SIGNUP, param2)
            fragment.arguments = args
            return fragment
        }
    }

    internal fun hitApi(isTerms: Int) {
        CommonUtil.showProgressBar(rl_progress_bar)
        if (isTerms == 1) {
            BackgroundExecutor.instance.execute(TermsAndAboutRequester(TermsAboutRequest(AppConstant.TERMS_IDENTIFIER)))
        } else if (isTerms == 2) {
            BackgroundExecutor.instance.execute(TermsAndAboutRequester(TermsAboutRequest(AppConstant.CONTACT)))
        } else if (isTerms == 3) {
            BackgroundExecutor.instance.execute(TermsAndAboutRequester(TermsAboutRequest(AppConstant.HELP)))
        } else if (isTerms == 4) {
            BackgroundExecutor.instance.execute(TermsAndAboutRequester(TermsAboutRequest(AppConstant.SUPPORT)))
        } else if (isTerms == 5) {
            BackgroundExecutor.instance.execute(TermsAndAboutRequester(TermsAboutRequest(AppConstant.PRIVACY)))
        }
    }

}// Required empty public constructor
