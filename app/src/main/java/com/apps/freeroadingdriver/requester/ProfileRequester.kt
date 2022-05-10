package com.apps.freeroadingdriver.requester

import android.util.Log

import com.apps.freeroadingdriver.constants.AppConstant
import com.apps.freeroadingdriver.eventbus.EventConstant
import com.apps.freeroadingdriver.eventbus.EventObject
import com.apps.freeroadingdriver.manager.UserManager
import com.apps.freeroadingdriver.model.dataModel.Profile
import com.apps.freeroadingdriver.model.requestModel.BaseRequest
import com.apps.freeroadingdriver.model.requestModel.EditProfileRequest
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import com.apps.freeroadingdriver.network.CZResponse
import com.apps.freeroadingdriver.network.HTTPOperationController

import org.greenrobot.eventbus.EventBus

/**
 * Created by Harshil on 12/20/2017.
 */

class ProfileRequester(internal var makeDefaultCardRequest: BaseRequest) : Runnable {
    private val TAG = ProfileRequester::class.java.name

    override fun run() {
        Log.d(TAG, "on run method")
        val czResponse = HTTPOperationController.fetchProfile(makeDefaultCardRequest)
        if (czResponse != null) {
            val response = czResponse.response as BaseResponse
            if (response.response_status == AppConstant.STATUS_SUCCESS) {
                if (response.response_data!!.profile != null) {
                    val userProfile = response.response_data!!.profile
                    UserManager.getInstance().user = userProfile
                }
                EventBus.getDefault().post(EventObject(EventConstant.PROFILE_SUCCESS, czResponse.response))
            } else if (response.response_status == AppConstant.STATUS_FAILURE) {
                EventBus.getDefault().post(EventObject(EventConstant.PROFILE_ERROR, czResponse.response))
            }
        } else {
            EventBus.getDefault().post(EventObject(EventConstant.SERVER_ERROR, ""))
        }
    }
}
