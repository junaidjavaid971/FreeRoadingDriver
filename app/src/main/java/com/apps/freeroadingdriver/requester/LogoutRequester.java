package com.apps.freeroadingdriver.requester;



import android.util.Log;

import com.apps.freeroadingdriver.constants.AppConstant;
import com.apps.freeroadingdriver.eventbus.EventConstant;
import com.apps.freeroadingdriver.eventbus.EventObject;
import com.apps.freeroadingdriver.model.requestModel.BaseRequest;
import com.apps.freeroadingdriver.model.responseModel.BaseResponse;
import com.apps.freeroadingdriver.network.CZResponse;
import com.apps.freeroadingdriver.network.HTTPOperationController;
import com.apps.freeroadingdriver.prefrences.FreeRoadingPreferenceManager;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Admin on 7/10/2017.
 */

public class LogoutRequester implements Runnable {
    public static String TAG = LogoutRequester.class.getName();

    private BaseRequest logoutRequest;

    public LogoutRequester(BaseRequest logoutRequest){
        this.logoutRequest = logoutRequest;
    }

    @Override
    public void run() {
        Log.d(TAG,"run method called");
        CZResponse<BaseResponse> czResponse = HTTPOperationController.logout(logoutRequest);
        if(czResponse != null){
            BaseResponse response = (BaseResponse) czResponse.getResponse();
            if (response.getResponse_status() == AppConstant.STATUS_SUCCESS) {
                EventBus.getDefault().post(new EventObject(EventConstant.LOGOUT_SUCCESS,response));
                FreeRoadingPreferenceManager.getInstance().setLogin(false);
                FreeRoadingPreferenceManager.getInstance().logoutUser();
            } else if (response.getResponse_status() == AppConstant.STATUS_FAILURE) {
                EventBus.getDefault().post(new EventObject(EventConstant.LOGOUT_ERROR,response));
            }
        } else {
            EventBus.getDefault().post(new EventObject(EventConstant.SERVER_ERROR, ""));
        }
    }
}
