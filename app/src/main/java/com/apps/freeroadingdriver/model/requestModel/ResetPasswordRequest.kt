package com.apps.freeroadingdriver.model.requestModel


import com.apps.freeroadingdriver.constants.AppConstant

/**
 * Created by Admin on 9/26/2017.
 */

class ResetPasswordRequest(private val confirm_password: String, private val password: String, private val driver_id: String, private val otp: String) {
    private val language: String

    init {
        this.language = AppConstant.APP_LANGUAGE
    }
}
