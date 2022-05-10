package com.apps.freeroadingdriver.requester;

import android.util.Log;

import com.apps.freeroadingdriver.constants.AppConstant;
import com.apps.freeroadingdriver.eventbus.EventConstant;
import com.apps.freeroadingdriver.eventbus.EventObject;
import com.apps.freeroadingdriver.model.requestModel.TripHistoryRequest;
import com.apps.freeroadingdriver.model.responseModel.BaseResponse;
import com.apps.freeroadingdriver.network.CZResponse;
import com.apps.freeroadingdriver.network.HTTPOperationController;

import org.greenrobot.eventbus.EventBus;

public class TripHistoryRequester implements Runnable {
    private String TAG = TripHistoryRequester.class.getName();
    private final TripHistoryRequest request;

    public TripHistoryRequester(TripHistoryRequest request) {
        this.request = request;
    }

    @Override
    public void run() {
        Log.d(TAG, "run method called");
        CZResponse<BaseResponse> czResponse = HTTPOperationController.getTripHistory(request);
        if (czResponse != null) {
            BaseResponse response = (BaseResponse) czResponse.getResponse();
            if (response.getResponse_status() == AppConstant.STATUS_SUCCESS) {
                EventBus.getDefault().post(new EventObject(EventConstant.GET_RIDE_HISTORY_SUCCESS, czResponse.getResponse()));
            } else if (response.getResponse_status() == AppConstant.STATUS_FAILURE) {
                EventBus.getDefault().post(new EventObject(EventConstant.GET_RIDE_HISTORY_ERROR, czResponse.getResponse()));
            }
        } else {
            EventBus.getDefault().post(new EventObject(EventConstant.SERVER_ERROR, ""));
        }
    }
}
