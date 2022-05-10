package com.apps.freeroadingdriver.model.requestModel

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Harshil on 11/28/2017.
 */

class SignUpRequest : BaseRequest, Parcelable {
    var name: String? = null
    var email: String? = null
    var password: String? = null
    var mobile: String? = null
    var profile_pic: String? = null
    var country_code: String? = null
    var otp: String? = null
    var request_type: String? = null
    var confirm_password: String? = null


    constructor() : super(true, false, true, true) {}

    constructor(isSessionToken: Boolean, isDeviceId: Boolean) : super(false, isSessionToken, isDeviceId, false) {}
    constructor(`in`: Parcel) : super(`in`) {
        name = `in`.readString()
        email = `in`.readString()
        password = `in`.readString()
        mobile = `in`.readString()
        profile_pic = `in`.readString()
        country_code = `in`.readString()
        otp = `in`.readString()
        request_type = `in`.readString()
        confirm_password = `in`.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeString(name)
        dest.writeString(email)
        dest.writeString(password)
        dest.writeString(mobile)
        dest.writeString(profile_pic)
        dest.writeString(country_code)
        dest.writeString(otp)
        dest.writeString(request_type)
        dest.writeString(confirm_password)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {

        val CREATOR: Parcelable.Creator<SignUpRequest> = object : Parcelable.Creator<SignUpRequest> {
            override fun createFromParcel(`in`: Parcel): SignUpRequest {
                return SignUpRequest(`in`)
            }

            override fun newArray(size: Int): Array<SignUpRequest?> {
                return arrayOfNulls(size)
            }
        }
    }
}
