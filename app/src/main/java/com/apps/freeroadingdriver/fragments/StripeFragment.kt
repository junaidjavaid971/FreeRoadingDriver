package com.apps.freeroadingdriver.fragments


import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient

import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.activities.BaseActivity
import com.apps.freeroadingdriver.activities.DashboardActivity
import com.apps.freeroadingdriver.constants.AppConstant
import com.apps.freeroadingdriver.constants.CommonMethods
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.manager.UserManager
import com.apps.freeroadingdriver.model.requestModel.UpdateStripeRequest
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import com.apps.freeroadingdriver.prefrences.FreeRoadingPreferenceManager
import com.apps.freeroadingdriver.requester.BackgroundExecutor
import com.apps.freeroadingdriver.requester.UpdateStripeRequester
import com.apps.freeroadingdriver.utils.CommonUtil
import com.apps.freeroadingdriver.utils.DialogUtil
import kotlinx.android.synthetic.main.fragment_stripe.*
import kotlinx.android.synthetic.main.progress_bar_layout.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StripeFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class StripeFragment : BaseFragment() {
    @Subscribe override fun onEvent(eventObject: EventObject) {
        if(eventObject==null){
            return
        }
        requireActivity().runOnUiThread(Runnable {
            try {
                CommonUtil.hideProgressBar(rl_progress_bar)
                onHandleBaseEvent(eventObject)
                var response = eventObject.`object` as BaseResponse
                when(eventObject.id){
                    EventConstant.STRIPE_UPDATE_SUCCESS -> {
                        EventBus.getDefault().post(EventObject(EventConstant.STRIPE_HOME_PAGE_UPDATE,response))
                        FreeRoadingPreferenceManager.getInstance().stripeToken = "1"
                        CommonMethods.showLongToast(context,response.response_msg)
                        (context as BaseActivity)?.onBackPressed()
                    }
                    EventConstant.STRIPE_UPDATE_ERROR -> {
                        DialogUtil.showOkButtonDialog(context,"Alert",response.response_msg,response.response_key)
                    }
                }
            }catch (e:Exception){
             e.printStackTrace()
            }
        })

    }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stripe, container, false)
    }

    private lateinit var jsonStripeString: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView?.getSettings()?.setJavaScriptEnabled(true)
        webView?.loadUrl(AppConstant.WEB_STRIPE)
        CommonUtil.showProgressBar(rl_progress_bar)

        webView.setWebViewClient(object : WebViewClient() {

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Log.d("mainactivity_result", url)

               // if (url.startsWith("stripeconnecttest://?result="))
                if (url.startsWith("stripeconnecttest://"))
                {
                    view.stopLoading();
                    view==null;
                    jsonStripeString = url.substring(url.lastIndexOf("=") + 1)
                    if (isJSONValid(jsonStripeString)) {
                        try {
                            val jsonValue = JSONObject(jsonStripeString)
                            updateStripeDataToServer(jsonValue)

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            override fun onPageFinished(view: WebView, url: String) {
                Log.d("mainactivity_result", url)
                CommonUtil.hideProgressBar(rl_progress_bar)
            }
        })
    }

    private fun isJSONValid(test: String?): Boolean {
        try {
            JSONObject(test)
        } catch (ex: JSONException) {
            try {
                JSONArray(test)
            } catch (ex1: JSONException) {
                return false
            }
        }
        return true
    }


    @Throws(JSONException::class)
    fun updateStripeDataToServer(`object`: JSONObject?) {
        CommonUtil.showProgressBar(rl_progress_bar)
//        FreeRoadingPreferenceManager.getInstance().stripeToken = `object`?.getString("stripe_user_id")
        BackgroundExecutor.instance.execute(UpdateStripeRequester(UpdateStripeRequest(`object`?.getString("stripe_user_id"),`object`?.getString("refresh_token"),`object`?.getString("access_token"),`object`?.getString("stripe_publishable_key"),`object`?.getString("scope"))))
    }


    override fun onDestroyView() {
        super.onDestroyView()
        if (context is DashboardActivity) {
            (context as DashboardActivity).setBackVisibility(false)
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StripeFragment.
         */
        val TAG = StripeFragment::class.java.name
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                StripeFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}
