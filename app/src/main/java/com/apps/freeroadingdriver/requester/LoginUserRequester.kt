package com.apps.freeroadingdriver.requester


import android.util.Log
import com.apps.freeroadingdriver.constants.AppConstant
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.manager.UserManager
import com.apps.freeroadingdriver.model.requestModel.LoginRequest
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import com.apps.freeroadingdriver.network.HTTPOperationController
import com.apps.freeroadingdriver.prefrences.FreeRoadingPreferenceManager
import org.greenrobot.eventbus.EventBus

/**
 * Created by admin on 6/21/2017.
 */

class LoginUserRequester(private val login: LoginRequest) : Runnable {
    private val TAG = LoginUserRequester::class.java.name

    override fun run() {
        Log.d(TAG, "run method called")
        val czResponse = HTTPOperationController.loginUser(login)
        if (czResponse != null) {
            val response = czResponse.response as BaseResponse
            if (response.response_status == AppConstant.STATUS_SUCCESS) {
                EventBus.getDefault().post(EventObject(EventConstant.LOGIN_SUCCESS, response))
                UserManager.getInstance().user = response.response_data!!.profile
                FreeRoadingPreferenceManager.getInstance().setLogin(true)
                FreeRoadingPreferenceManager.getInstance().sessionToken = response.response_data!!.session_token
//                FreeRoadingPreferenceManager.getInstance().setServerChannel(response.response_data!!.getSer_chn())
//                val vehicle = GsonUtil.toJson(response.response_data!!.getVehilce_type().getType())
//                FreeRoadingPreferenceManager.getInstance().vehicleType = vehicle
//                if (response.response_data != null && response.response_data!!.getUser_profile() != null)
//                    FreeRoadingPreferenceManager.getInstance().referalCode = response.response_data!!.getUser_profile().getReferral_code()
            } else if (response.response_status == AppConstant.STATUS_FAILURE) {
                EventBus.getDefault().post(EventObject(EventConstant.LOGIN_ERROR, czResponse.response))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}
