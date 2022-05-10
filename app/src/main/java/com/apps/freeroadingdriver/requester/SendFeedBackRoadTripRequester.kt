package com.app.freeroading.requester

import android.util.Log
import com.app.freeroading.model.requestModel.InvoicePayTripAmout
import com.apps.freeroadingdriver.constants.AppConstant
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import com.apps.freeroadingdriver.network.HTTPOperationController
import org.greenrobot.eventbus.EventBus

/**
 * Created by Harshil on 3/30/2018.
 */
class SendFeedBackRoadTripRequester(internal var addRoadTrip : InvoicePayTripAmout) : Runnable {
    private val TAG = SendFeedBackRoadTripRequester::class.java.name

    override fun run() {
        Log.d(TAG, "on run method")
        val czResponse = HTTPOperationController.sendfeedbakc(addRoadTrip)
        if (czResponse != null) {
            val response = czResponse.response as BaseResponse
            if (response.response_status == AppConstant.STATUS_SUCCESS) {
                EventBus.getDefault().post(EventObject(EventConstant.INVOICEPAYAMOUNT_SUCCESS, czResponse.response))
            } else if (response.response_status == AppConstant.STATUS_FAILURE) {
                EventBus.getDefault().post(EventObject(EventConstant.INVOICEPAYAMOUNT_ERROR, czResponse.response))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}