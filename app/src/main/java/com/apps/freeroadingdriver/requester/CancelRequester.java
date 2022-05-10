package com.apps.freeroadingdriver.requester;


import android.util.Log;

import com.apps.freeroadingdriver.constants.AppConstant;
import com.apps.freeroadingdriver.eventbus.EventConstant;
import com.apps.freeroadingdriver.eventbus.EventObject;
import com.apps.freeroadingdriver.model.requestModel.CancelRequest;
import com.apps.freeroadingdriver.model.responseModel.BaseResponse;
import com.apps.freeroadingdriver.network.CZResponse;
import com.apps.freeroadingdriver.network.HTTPOperationController;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Harshil on 11/30/2017.
 */

public class CancelRequester implements Runnable {
    private String TAG = CancelRequester.class.getName();
    CancelRequest checkOtpRequest;

    public CancelRequester(CancelRequest checkOtpRequest){
        this.checkOtpRequest = checkOtpRequest;
    }

    @Override
    public void run() {
        Log.d(TAG, "run method called");
        CZResponse<BaseResponse> czResponse = HTTPOperationController.cancelRequest(checkOtpRequest);
        if (czResponse != null) {
            BaseResponse response = (BaseResponse) czResponse.getResponse();
            if (response.getResponse_status() == AppConstant.STATUS_SUCCESS) {
                EventBus.getDefault().post(new EventObject(EventConstant.CANCEL_SUCCESS, response));
            } else if (response.getResponse_status() == AppConstant.STATUS_FAILURE) {
                EventBus.getDefault().post(new EventObject(EventConstant.CANCEL_ERROR, response));
            }
        } else {
            EventBus.getDefault().post(new EventObject(EventConstant.SERVER_ERROR, ""));
        }
    }
}
