package com.apps.freeroadingdriver.requester


import android.util.Log
import com.apps.freeroadingdriver.constants.AppConstant
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.manager.UserManager
import com.apps.freeroadingdriver.model.requestModel.SignUpRequest
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import com.apps.freeroadingdriver.network.HTTPOperationController
import com.apps.freeroadingdriver.prefrences.FreeRoadingPreferenceManager


import org.greenrobot.eventbus.EventBus

class SignUpUserRequester(private val signIn: SignUpRequest) : Runnable {
    private val TAG = SignUpUserRequester::class.java.name

    override fun run() {
        Log.d(TAG, "run method called")
        val czResponse = HTTPOperationController.signinUser(signIn)
        if (czResponse != null) {
            val response = czResponse.response as BaseResponse
            if (response.response_status == AppConstant.STATUS_SUCCESS) {
                if (response.response_data!!.profile != null) {
                    val userProfile = response.response_data!!.profile
                    UserManager.getInstance().user = userProfile
                    FreeRoadingPreferenceManager.getInstance().setLogin(true)
                }
                EventBus.getDefault().post(EventObject(EventConstant.SIGN_UP_SUCCESS, response))

                //                String vehicle= GsonUtil.toJson(response.getResponse_data().getVehilce_type());
                //                TaxiShakePreferenceManager.getInstance().setVehicleType(vehicle);
                FreeRoadingPreferenceManager.getInstance().sessionToken = response.response_data!!.session_token
            } else if (response.response_status == AppConstant.STATUS_FAILURE) {
                EventBus.getDefault().post(EventObject(EventConstant.SIGN_UP_ERROR, response))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}
