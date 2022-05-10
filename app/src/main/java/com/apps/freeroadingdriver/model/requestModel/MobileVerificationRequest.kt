package com.apps.freeroadingdriver.model.requestModel

import com.apps.freeroadingdriver.constants.AppConstant

class MobileVerificationRequest {
    private var email: String? = null
    private var country_code: String? = null
    private var language: String? = null
    private var mobile: String = ""

    constructor(email: String) {
        this.email = email
        this.language = AppConstant.APP_LANGUAGE
    }

    constructor(country_code: String, mobile: String) {
        this.country_code = country_code
        this.language = AppConstant.APP_LANGUAGE
        this.mobile = mobile
    }
}
