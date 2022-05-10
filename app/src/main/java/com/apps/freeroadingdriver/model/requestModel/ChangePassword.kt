package com.apps.freeroadingdriver.model.requestModel

/**
 * Created by Harshil on 3/26/2018.
 */

class ChangePassword(internal var newpassword: String, internal var oldpassword: String) : BaseRequest(false, true, false, false)
