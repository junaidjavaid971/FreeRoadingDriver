package com.apps.freeroadingdriver.requester

import android.util.Log
import com.apps.freeroadingdriver.constants.AppConstant
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.model.requestModel.MobileVerificationRequest
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import com.apps.freeroadingdriver.network.HTTPOperationController

import org.greenrobot.eventbus.EventBus

/**
 * Created by Admin on 9/26/2017.
 */

class MobileVerificationRequester(private val mobileVerificationRequest: MobileVerificationRequest) : Runnable {
    private val TAG = MobileVerificationRequester::class.java.name

    override fun run() {
        Log.d(TAG, "run method called")
        val czResponse = HTTPOperationController.mobileVerification(mobileVerificationRequest)
        if (czResponse != null) {
            val response = czResponse.response as BaseResponse
            if (response.response_status == AppConstant.STATUS_SUCCESS) {
                EventBus.getDefault().post(EventObject(EventConstant.MOBILE_VERIFICATION_SUCCESS, response))
            } else if (response.response_status == AppConstant.STATUS_FAILURE) {
                EventBus.getDefault().post(EventObject(EventConstant.MOBILE_VERIFICATION_ERROR, response))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }

    }
}
