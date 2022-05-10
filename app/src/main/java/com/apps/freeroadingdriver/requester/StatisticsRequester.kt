package com.apps.freeroadingdriver.requester


import android.util.Log

import com.apps.freeroadingdriver.constants.AppConstant
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.model.requestModel.BaseRequest
import com.apps.freeroadingdriver.model.requestModel.ChangeAvailableRequest
import com.apps.freeroadingdriver.model.requestModel.StatisticsRequest
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import com.apps.freeroadingdriver.network.CZResponse
import com.apps.freeroadingdriver.network.HTTPOperationController

import org.greenrobot.eventbus.EventBus

/**
 * Created by Harshil on 12/20/2017.
 */

class StatisticsRequester(internal var makeDefaultCardRequest: StatisticsRequest) : Runnable {
    private val TAG = StatisticsRequester::class.java.name

    override fun run() {
        Log.d(TAG, "on run method")
        val czResponse = HTTPOperationController.statistics(makeDefaultCardRequest)
        if (czResponse != null) {
            val response = czResponse.response as BaseResponse
            if (response.response_status == AppConstant.STATUS_SUCCESS) {
                EventBus.getDefault().post(EventObject(EventConstant.STATISTICS_SUCCESS, czResponse.response))
            } else if (response.response_status == AppConstant.STATUS_FAILURE) {
                EventBus.getDefault().post(EventObject(EventConstant.STATISTICS_ERROR, czResponse.response))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}
