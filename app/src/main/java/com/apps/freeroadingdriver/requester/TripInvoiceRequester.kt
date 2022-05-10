package com.app.freeroading.requester

import android.util.Log
import com.app.freeroading.model.requestModel.TripInvoiceRequest
import com.apps.freeroadingdriver.constants.AppConstant
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import com.apps.freeroadingdriver.network.HTTPOperationController


import org.greenrobot.eventbus.EventBus

/**
 * Created by Harshil on 12/20/2017.
 */

class TripInvoiceRequester(internal var makeDefaultCardRequest: TripInvoiceRequest) : Runnable {
    private val TAG = TripInvoiceRequester::class.java.name

    override fun run() {
        Log.d(TAG, "on run method")
        val czResponse = HTTPOperationController.tripInvoice(makeDefaultCardRequest)
        if (czResponse != null) {
            val response = czResponse.response as BaseResponse
            if (response.response_status == AppConstant.STATUS_SUCCESS) {
                EventBus.getDefault().post(EventObject(EventConstant.TRIPINVOICE_SUCCESS, czResponse.response))
            } else if (response.response_status == AppConstant.STATUS_FAILURE) {
                EventBus.getDefault().post(EventObject(EventConstant.TRIPINVOICE_ERROR, czResponse.response))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}
