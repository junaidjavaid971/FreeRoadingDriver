package com.apps.freeroadingdriver.prefrences;

import android.content.SharedPreferences;
import android.util.Log;
import com.apps.freeroadingdriver.model.dataModel.Profile;
import com.apps.freeroadingdriver.FreeRoadingApp;



public class FreeRoadingPreferenceManager extends BasePreferences {
    public final static String TAG = FreeRoadingPreferenceManager.class.getSimpleName();
    private static final String SHARED_PREF_NAME = "TaxiShakePassenger_pref";
    private static final int PRIVATE_MODE = 0;
    private static FreeRoadingPreferenceManager mInstance;

    private FreeRoadingPreferenceManager() {
        super(FreeRoadingApp.getInstance().
                getSharedPreferences(SHARED_PREF_NAME, PRIVATE_MODE));
    }

    public static FreeRoadingPreferenceManager getInstance() {
        if (mInstance == null) {
            synchronized (FreeRoadingPreferenceManager.class) {
                if (mInstance == null) {
                    mInstance = new FreeRoadingPreferenceManager();
                }
            }
        }
        return mInstance;
    }

    public void setLanguage(String language) {
        setString(Keys.LANGUAGE, language);
    }



    public String getLanguage() {
        return  getString(Keys.LANGUAGE, "en");
    }

    public boolean getRideRoadType() {
        return getBoolean(Keys.IS_RIDE_ROAD_TYPE, false);
    }

    public void setRideRoadType(boolean isride) {
        Log.d(TAG, "save ride active value: " + isride);
        setBoolean(Keys.IS_RIDE_ROAD_TYPE, isride);
    }

    public boolean getRideActive() {
        return getBoolean(Keys.IS_RIDE_ACTIVE, false);
    }

    public void setRideActive(boolean isride) {
        Log.d(TAG, "save ride active value: " + isride);
        setBoolean(Keys.IS_RIDE_ACTIVE, isride);
    }

    public String getStripeToken() {
        return getString(Keys.STRIPE_TOKEN, null);
    }

    public void setStripeToken(String token) {
        setString(Keys.STRIPE_TOKEN, token);
    }

    public String getRideStatus() {
        return getString(Keys.RIDE_STATUS, null);
    }

    public void setRideStatus(String rideStatus) {
        setString(Keys.RIDE_STATUS, rideStatus);
    }

    public String getEstimatedTime() {
        return getString(Keys.Estimated_Arrival_Time, null);
    }

    public void setEstimatedTime(String time) {
        Log.d(TAG, "save estimated time value: " + time);
        setString(Keys.Estimated_Arrival_Time, time);
    }

    public double getDistance() {
        return getDouble(Keys.TRAVAL_DISTANCE, 0.0);
    }

    public void setDistance(double country_code) {
        Log.d(TAG, "save profile pic url value: " + country_code);
        setDouble(Keys.TRAVAL_DISTANCE, country_code);
    }

    public void setAvailability(String availability) {
        Log.d(TAG, "save login status value: " + availability);
        setString(Keys.IS_AVAILABILITY, availability);
    }

    public String isAvailability() {
        return getString(Keys.IS_AVAILABILITY, "");

    }

    public String getReferalCode() {
        return getString(Keys.REFERAL_CODE, "");
    }

    public void setReferalCode(String id) {
        setString(Keys.REFERAL_CODE, id);
    }

    public String getAppointmentId() {
        return getString(Keys.APPOINTMENT_ID, "");
    }

    public void setAppointmentId(String id) {
        setString(Keys.APPOINTMENT_ID, id);
    }

    public int getActionId() {
        return getInt(Keys.ACTION_ID, 0);
    }

    public void setActionId(int id) {
        setInt(Keys.ACTION_ID, id);
    }

    public String getVehicleType() {
        return getString(Keys.VEHICLE_TYPE, "");
    }

    public void setVehicleType(String vehicle) {
        setString(Keys.VEHICLE_TYPE, vehicle);
    }

    public String getType() {
        return getString(Keys.TYPE, "");
    }

    public void setType(String vehicle) {
        setString(Keys.TYPE, vehicle);
    }

    public String getPubNubChan() {
        return getString(Keys.PUBNUB_CHANNEL, "");
    }

    public void setPubNubChan(String channel) {
        setString(Keys.PUBNUB_CHANNEL, channel);
    }

    public void setServerChannel(String channel) {
        setString(Keys.PUBNUB_SERVER_CHANNEL, channel);
    }

    public String getServerChan() {
        return getString(Keys.PUBNUB_SERVER_CHANNEL, "");
    }

    public String getDriverOnMap() {
        return getString(Keys.DRIVER, "");
    }

    public void setDriverOnMap(String driver) {
        setString(Keys.DRIVER, driver);
    }

    public void setDeviceId(String deviceId) {
        setString(Keys.DEVICE_ID, null);
    }

    public String getDeviceID() {
        return getString(Keys.DEVICE_ID, null);
    }

    public String getDeviceToken() {
        return getString(Keys.DEVICE_TOKEN, null);
    }

    public void setDeviceToken(String token) {
        Log.d(TAG, "save device token value: " + token);
        setString(Keys.DEVICE_TOKEN, token);
    }

    public String getSessionToken() {
        return getString(Keys.SESSION_TOKEN, null);
    }

    public void setSessionToken(String token) {
        Log.d(TAG, "save session token value: " + token);
        setString(Keys.SESSION_TOKEN, token);
    }

    public long getUserId() {
        return getLong(Keys.USER_ID, 0);
    }

    public void setUserId(long userId) {
        Log.d(TAG, "save user id value: " + userId);
        setLong(Keys.USER_ID, userId);
    }

    public String getEmail() {
        return getString(Keys.EMAIL, "");
    }

    public void setEmail(String email) {
        Log.d(TAG, "save email value: " + email);
        setString(Keys.EMAIL, email);
    }

    public String getName() {
        return getString(Keys.NAME, "USER");
    }

    public void setName(String name) {
        Log.d(TAG, "save name value: " + name);
        setString(Keys.NAME, name);
    }

    public long getMobileNumber() {
        return getLong(Keys.MOBILE_NO, 0);
    }

    public void setMobileNumber(long mobileNumber) {
        Log.d(TAG, "save mobile number  value: " + mobileNumber);
        setLong(Keys.MOBILE_NO, mobileNumber);
    }

    public String getCountryCode() {
        return getString(Keys.COUNTRY_CODE, "");
    }

    public void setCountryCode(String code) {
        Log.d(TAG, "save country code value: " + code);
        setString(Keys.COUNTRY_CODE, code);
    }

    public boolean isLogin() {
        return getBoolean(Keys.IS_LOGIN, false);
    }

    public void setLogin(Boolean login) {
        Log.d(TAG, "save login status value: " + login);
        setBoolean(Keys.IS_LOGIN, login);
    }

    public String getProfileUrl() {
        return getString(Keys.PROFILE_URL, "");
    }

    public void setProfileUrl(String profileUrl) {
        Log.d(TAG, "save profile pic url value: " + profileUrl);
        setString(Keys.PROFILE_URL, profileUrl);
    }


    public void setAddRideStatus(boolean is_status)
    {
        setBoolean(Keys.IS_ADD_RIDE_STATUS,is_status);
    }
    public boolean getAddRideStatus()
    {
        return getBoolean(Keys.IS_ADD_RIDE_STATUS,true);
    }

    public Profile getUserDetail() {
        Profile user = new Profile();
        user.setEmail(getEmail());
        user.setDriver_id(String.valueOf(getUserId()));
        user.setMobile(String.valueOf(getMobileNumber()));
        user.setProfile_pic(getProfileUrl());
        user.setName(getName());
        user.setCountry_code(getCountryCode());
        user.setPub_chn(getPubNubChan());
        user.setAvailable_status(String.valueOf(isAvailability()));
        return user;
    }

    public void setUserDetail(Profile userDetail) {
        setUserId(Long.parseLong(userDetail.getDriver_id()));
        setEmail(userDetail.getEmail());
        setName(userDetail.getName());
        setMobileNumber(Long.parseLong(userDetail.getMobile()));
        setProfileUrl(userDetail.getProfile_pic());
        setCountryCode(userDetail.getCountry_code());
        setPubNubChan(userDetail.getPub_chn());
        setAvailability(userDetail.getAvailable_status());
        setLogin(true);
    }

    public void logoutUser() {
        SharedPreferences.Editor editor = getmPref().edit();
        editor.remove(Keys.SESSION_TOKEN);
        editor.remove(Keys.IS_LOGIN);
        editor.remove(Keys.USER_ID);
        editor.remove(Keys.MOBILE_NO);
        editor.remove(Keys.NAME);
        editor.remove(Keys.PROFILE_URL);
        editor.apply();
    }

    private interface Keys {
        String DEVICE_TOKEN = "device_token";
        String SESSION_TOKEN = "session_token";
        String DEVICE_ID = "device_id";
        String DEVICE_TYPE = "device_type";
        String USER_ID = "user_id";
        String EMAIL = "email";
        String NAME = "name";
        String MOBILE_NO = "mobile_number";
        String COUNTRY_CODE = "country_code";
        String PROFILE_URL = "profile_url";
        String GENDER = "gender";
        String IS_LOGIN = "is_login";
        String VEHICLE_TYPE = "vehicle_type";
        String TYPE = "type";
        String DRIVER = "driver";
        String PUBNUB_CHANNEL = "pub_chn";
        String PUBNUB_SERVER_CHANNEL = "ser_chn";
        String APPOINTMENT_ID = "appointment_id";
        String ACTION_ID = "action_id";
        String REFERAL_CODE = "referal_code";
        String IS_AVAILABILITY = "is_availability";
        String TRAVAL_DISTANCE = "distance";
        String Estimated_Arrival_Time = "estimated_time";
        String RIDE_STATUS = "ride_status";
        String STRIPE_TOKEN = "stripe_token";
        String IS_RIDE_ACTIVE = "is_ride_active";
        String IS_RIDE_ROAD_TYPE = "is_ride_road_type";
        String LANGUAGE = "language";
        String IS_ADD_RIDE_STATUS="is_add_ride_status";
    }


}
