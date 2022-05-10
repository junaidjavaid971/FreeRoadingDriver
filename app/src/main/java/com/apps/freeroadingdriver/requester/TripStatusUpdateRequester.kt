package com.apps.freeroadingdriver.requester

import android.util.Log
import com.app.freeroading.model.requestModel.TripStatusUpdateRequest
import com.apps.freeroadingdriver.constants.AppConstant
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import com.apps.freeroadingdriver.network.HTTPOperationController


import org.greenrobot.eventbus.EventBus


class TripStatusUpdateRequester(internal var makeDefaultCardRequest: TripStatusUpdateRequest) : Runnable {
    private val TAG = TripStatusUpdateRequester::class.java.name

    override fun run() {
        Log.d(TAG, "on run method")
        val czResponse = HTTPOperationController.tripStatusUpdate(makeDefaultCardRequest)
        if (czResponse != null) {
            val response = czResponse.response as BaseResponse
            if (response.response_status == AppConstant.STATUS_SUCCESS) {
                EventBus.getDefault().post(EventObject(EventConstant.TRIPSTATUSUPDATE_SUCCESS, czResponse.response))
            } else if (response.response_status == AppConstant.STATUS_FAILURE) {
                EventBus.getDefault().post(EventObject(EventConstant.TRIPSTATUSUPDATE_ERROR, czResponse.response))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}
