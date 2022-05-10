package com.apps.freeroadingdriver.model.requestModel;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.apps.freeroadingdriver.FreeRoadingApp;
import com.apps.freeroadingdriver.constants.AppConstant;
import com.apps.freeroadingdriver.manager.LocationManagerWIthGps;
import com.apps.freeroadingdriver.prefrences.FreeRoadingPreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by admin on 6/23/2017.
 */

public class BaseRequest implements Parcelable {
    private String device_type;
    private String device_token;
    private String device_id;
    private String language;
    private double latitude;
    private double longitude;
    private String session_token;
    private int debug_mode;

    protected BaseRequest(Parcel in) {
        device_type = in.readString();
        device_token = in.readString();
        device_id = in.readString();
        language = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        session_token = in.readString();
        debug_mode = in.readInt();
    }

    public static final Creator<BaseRequest> CREATOR = new Creator<BaseRequest>() {
        @Override
        public BaseRequest createFromParcel(Parcel in) {
            return new BaseRequest(in);
        }

        @Override
        public BaseRequest[] newArray(int size) {
            return new BaseRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(device_type);
        dest.writeString(device_token);
        dest.writeString(device_id);
        dest.writeString(language);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(session_token);
        dest.writeInt(debug_mode);
    }


    public BaseRequest() {
        this.language = FreeRoadingPreferenceManager.getInstance().getLanguage();
        this.debug_mode = 1;
    }

    public BaseRequest(boolean isDeviceType) {
        this();
        if (isDeviceType) {
            this.device_type = AppConstant.DEVICE_TYPE;
        }
    }

    public BaseRequest(boolean isDeviceToken, boolean isSessionToken, boolean isDeviceID, boolean isLatLong) {
        this(true);
        if (isDeviceToken) {
            try {
                this.device_token = FreeRoadingPreferenceManager.getInstance().getDeviceToken();
                if (this.device_token == null || this.device_token.equals("")) {
                    FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (task.isSuccessful()) {
                                device_token = task.getResult();
                                FreeRoadingPreferenceManager.getInstance().setDeviceToken(device_token);
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (isSessionToken) {
            this.session_token = FreeRoadingPreferenceManager.getInstance().getSessionToken();
        }
        if (isDeviceID) {
            this.device_id = FreeRoadingApp.getInstance().getDeviceID();
        }
        if (isLatLong) {
            this.latitude = LocationManagerWIthGps.getInstance().getLatitude();
            this.longitude = LocationManagerWIthGps.getInstance().getLongitude();
        }
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getDevice_token() {
        if (device_token == null || device_token.equals("")) {
            try {
                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {
                            device_token = task.getResult();
                            FreeRoadingPreferenceManager.getInstance().setDeviceToken(device_token);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getDebug_mode() {
        return debug_mode;
    }

    public void setDebug_mode(int debug_mode) {
        this.debug_mode = debug_mode;
    }

}
