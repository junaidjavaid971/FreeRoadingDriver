package com.apps.freeroadingdriver.requester;


import android.util.Log;

import com.apps.freeroadingdriver.constants.AppConstant;
import com.apps.freeroadingdriver.eventbus.EventConstant;
import com.apps.freeroadingdriver.eventbus.EventObject;
import com.apps.freeroadingdriver.model.requestModel.BaseRequest;
import com.apps.freeroadingdriver.model.responseModel.BaseResponse;
import com.apps.freeroadingdriver.network.CZResponse;
import com.apps.freeroadingdriver.network.HTTPOperationController;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Admin on 7/10/2017.
 */

public class UserRideStatusRequester implements Runnable {
    public static String TAG = UserRideStatusRequester.class.getName();

    private BaseRequest rideStatusRequest;

    public UserRideStatusRequester(BaseRequest rideStatusRequest){
        this.rideStatusRequest = rideStatusRequest;
    }

    @Override
    public void run() {
        Log.d(TAG,"run method called");
        CZResponse<BaseResponse> czResponse = HTTPOperationController.getHomedData(rideStatusRequest);
        if(czResponse != null){
            BaseResponse response = (BaseResponse) czResponse.getResponse();
            if (response.getResponse_status() == AppConstant.STATUS_SUCCESS) {
                EventBus.getDefault().post(new EventObject(EventConstant.GET_HOME_DATA_SUCCESS,response));
            } else if (response.getResponse_status() == AppConstant.STATUS_FAILURE) {
                EventBus.getDefault().post(new EventObject(EventConstant.GET_HOME_DATA_ERROR,response));
            }
        } else {
             EventBus.getDefault().post(new EventObject(EventConstant.SERVER_ERROR, ""));
        }
    }
}
