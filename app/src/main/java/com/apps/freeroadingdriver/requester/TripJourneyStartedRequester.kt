package com.apps.freeroadingdriver.requester

import android.util.Log
import com.apps.freeroadingdriver.constants.AppConstant
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.model.requestModel.TripJourneyStartRequest
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import com.apps.freeroadingdriver.network.HTTPOperationController
import org.greenrobot.eventbus.EventBus

/**
 * Created by Harshil on 3/30/2018.
 */
class TripJourneyStartedRequester(internal var addRoadTrip : TripJourneyStartRequest) : Runnable {
    private val TAG = TripJourneyStartedRequester::class.java.name

    override fun run() {
        Log.d(TAG, "on run method")
        val czResponse = HTTPOperationController.tripStart(addRoadTrip)
        if (czResponse != null) {
            val response = czResponse.response as BaseResponse
            if (response.response_status == AppConstant.STATUS_SUCCESS) {
                EventBus.getDefault().post(EventObject(EventConstant.TRIPJOURNEY_SUCCESS, czResponse.response))
            } else if (response.response_status == AppConstant.STATUS_FAILURE) {
                EventBus.getDefault().post(EventObject(EventConstant.TRIPJOURNEY_ERROR, czResponse.response))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}