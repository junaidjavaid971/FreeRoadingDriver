package com.apps.freeroadingdriver.model.requestModel

class EditProfileRequest : BaseRequest {

    var name: String? = null
    var email: String? = null
    var mobile: String? = null
    var profile_pic: String? = null
    var country_code: String? = null
    var otp: String? = null
    var home_addrs: String? = null
    var class_license_held: String? = null
    var driving_experience: String? = null
    var facebook_url: String? = null
    var hobbies: String? = null;
    var instagram_url: String? = null
    var interests: String? = null
    var languages_spoken: String? = null
    var linkedin_url: String? = null
    var music_preferences: String? = null
    var pet_friendly: String? = null
    var smoking: String? = null
    var driver_license_pic: String? = null
    var wheel_drive_type: String? = null
    var driver_license_no: String? = null
    var license_expiry_date: String? = null
    var license_issue_date: String? = null
    var state_issue: String? = null


    constructor(name: String?, email: String?, mobile: String?, profile_pic: String?, country_code: String?, otp: String?, home_addrs: String?, class_license_held: String?, driving_experience: String?, facebook_url: String?, hobbies: String?, instagram_url: String?, interests: String?, languages_spoken: String?, linkedin_url: String?, music_preferences: String?, pet_friendly: String?,
                smoking: String?,driver_license_pic:String?,wheel_drive_type:String?, driver_license_no: String?,license_expiry_date: String?, license_issue_date: String?,state_issue: String?)
            : super(false, true, false, false) {
        this.name = name
        this.email = email
        this.mobile = mobile
        this.profile_pic = profile_pic
        this.country_code = country_code
        this.otp = otp
        this.home_addrs = home_addrs
        this.class_license_held = class_license_held;
        this.driving_experience = driving_experience
        this.facebook_url = facebook_url
        this.hobbies = hobbies
        this.instagram_url = instagram_url
        this.languages_spoken = languages_spoken
        this.interests = interests
        this.linkedin_url = linkedin_url
        this.music_preferences = music_preferences
        this.pet_friendly = pet_friendly
        this.smoking = smoking
        this.driver_license_pic=driver_license_pic
        this.wheel_drive_type=wheel_drive_type
        this.driver_license_no=driver_license_no
        this.license_expiry_date=license_expiry_date
        this.license_issue_date=license_issue_date
        this.state_issue=state_issue
    }
}
