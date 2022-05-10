package com.apps.freeroadingdriver.network;

import android.util.Log;


import com.apps.freeroadingdriver.eventbus.EventConstant;
import com.apps.freeroadingdriver.eventbus.EventObject;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.HttpURLConnection;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by craterzone on 21/9/16.
 */

public class ConnectionUtil {


    private static final String TAG = ConnectionUtil.class.getName();

    public static CZResponse execute(Call call) {
        try {
            Response response = call.execute();
            Log.d(TAG, "Api request , request url : " + response.raw().request().url());
            Log.d(TAG, "Api request , response code : " + response.code());
            Log.d(TAG, "Api request , response body : " + response.body());
            if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                EventBus.getDefault().post(new EventObject(EventConstant.TOKEN_EXPIRE, 0));
                return null;
            }else if (response.code() == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                EventBus.getDefault().post(new EventObject(EventConstant.SERVER_ERROR, 0));
                return null;
            }
            return new CZResponse(response.code(), response.body(), response.errorBody(), response.headers());

        } catch (IOException e) {
            Log.d(TAG, "Error in execute api request");
            EventBus.getDefault().post(new EventObject(EventConstant.SERVER_ERROR, 0));
        }
        catch (Exception ex) {
            Log.d(TAG, "Error in execute api" + ex.getMessage());
            EventBus.getDefault().post(new EventObject(EventConstant.TOKEN_EXPIRE, 0));
        }
        return null;
    }
}
