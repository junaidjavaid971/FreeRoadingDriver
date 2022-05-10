package com.apps.freeroadingdriver.constants;

/**
 * Created by Harshil on 11/25/2017.
 */

public interface AppConstant {

    String TEMP_PHOTO_FILE_NAME = "temp.png";
    String APP_LANGUAGE = "en";
    String DEVICE_TYPE = "1";
    String USER_TYPE = "1";
    String SUV = "SUV";
    String SEDAN = "SEDAN";
    String PICKUP_TRUCK = "Pickup Truck";
    String FLATBED_VEHICLE = "Flatbed Vehicle";
    String TERMS = "terms";
    String CONTACT = "contact-us-driver";
    String PRIVACY = "privacy-driver";
    String HELP = "help-driver";
    String SUPPORT = "support-driver";
    String DISTANCE_UNIT = " Miles";
    String CURRENCY_UNIT = "$";
    String ABOUT_IDENTIFIER = "about_us_passenger";
    String TERMS_IDENTIFIER = "terms-condition-driver";
    String RIDE_REQUEST_COMES="ride-request-arrived";

 //   String PUBNUB_SUBSCRIPTION_KEY = "sub-c-17d6d486-21dd-11e8-be22-c2fd0b475b93";
   // String PUBNUB_PUBLISH_KEY = "pub-c-5ed58b18-0e31-4308-8483-466e7761973e";
//    String PUBNUB_SUBSCRIPTION_KEY = "sub-c-5dbafbc8-3b20-11e9-b682-2a55d2175413";
//    String PUBNUB_PUBLISH_KEY = "pub-c-b42e5db4-8599-454b-b250-33b565bb941c";

    String PUBNUB_SUBSCRIPTION_KEY = "sk_live_DWuR05fGLcH2XCi1WuFrRlG900Je38VePY";
    String PUBNUB_PUBLISH_KEY = "pk_live_gjqW2dSuMKH0vE5hYgfRdeGh";

//    String WEB_STRIPE = "https://connect.stripe.com/oauth/authorize?response_type=code&client_id=ca_EzasoHvgJCPJYMvs0REBIQKyWFykCPtc&scope=read_write";

      String WEB_STRIPE = "https://connect.stripe.com/express/oauth/authorize?response_type=code&client_id=ca_EzasJoDaFlWH147NDRwzzfLVlrTb72ji&scope=read_write";


      //String WEB_STRIPE = "https://connect.stripe.com/express/oauth/authorize?response_type=code&client_id=ca_EzasoHvgJCPJYMvs0REBIQKyWFykCPtc&scope=read_write";


    int STATUS_SUCCESS =1 ;
    int STATUS_FAILURE = 0;
    float MAP_ZOOM = 16.0f;
    String ARRIVED_STATUS = "4";
    String PICKED_STATUS = "3";
    String DROP_STATUS = "2";

    //put extra Constant
    String EMAIL_PASSED = "email_passed";
    String PASSENGER_ID = "passenger_id";
    String IS_ROAD_TRIP = "is_road_trip";
}
