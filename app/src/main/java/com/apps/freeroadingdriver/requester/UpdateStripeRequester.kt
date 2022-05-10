package com.apps.freeroadingdriver.requester


import android.util.Log

import com.apps.freeroadingdriver.constants.AppConstant
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.model.requestModel.UpdateStripeRequest
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import com.apps.freeroadingdriver.network.CZResponse
import com.apps.freeroadingdriver.network.HTTPOperationController

import org.greenrobot.eventbus.EventBus

/**
 * Created by Admin on 10/10/2017.
 */

class UpdateStripeRequester(private val request: UpdateStripeRequest) : Runnable {
    private val TAG = UpdateStripeRequester::class.java.name

    override fun run() {
        Log.d(TAG, "run method called")
        val czResponse = HTTPOperationController.updateStripeRequest(request)
        if (czResponse != null) {
            val response = czResponse.response as BaseResponse
            if (response.response_status == AppConstant.STATUS_SUCCESS) {
                EventBus.getDefault().post(EventObject(EventConstant.STRIPE_UPDATE_SUCCESS, czResponse.response))
            } else if (response.response_status == AppConstant.STATUS_FAILURE) {
                EventBus.getDefault().post(EventObject(EventConstant.STRIPE_UPDATE_ERROR, czResponse.response))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}
