package com.apps.freeroadingdriver.model.requestModel

import android.os.Parcel
import android.os.Parcelable


class LoginRequest : BaseRequest, Parcelable {

    var password: String? = null
    var mobile: String? = null

    constructor() : super(true, false, true, true) {}

    constructor(isSessionToken: Boolean, isDeviceId: Boolean) : super(false, isSessionToken, isDeviceId, false) {}

    constructor(password: String, email: String) : super(true, false, true, true) {
        this.password = password
        this.mobile = email
    }

    protected constructor(`in`: Parcel) : super(`in`) {
        password = `in`.readString()
        mobile = `in`.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeString(password)
        dest.writeString(mobile)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {

        val CREATOR: Parcelable.Creator<LoginRequest> = object : Parcelable.Creator<LoginRequest> {
            override fun createFromParcel(`in`: Parcel): LoginRequest {
                return LoginRequest(`in`)
            }

            override fun newArray(size: Int): Array<LoginRequest?> {
                return arrayOfNulls(size)
            }
        }
    }
}
