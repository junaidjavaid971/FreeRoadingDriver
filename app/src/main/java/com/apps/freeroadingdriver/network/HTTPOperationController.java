package com.apps.freeroadingdriver.network;
import com.app.freeroading.model.requestModel.InvoicePayTripAmout;
import com.app.freeroading.model.requestModel.TripFinishRequest;
import com.app.freeroading.model.requestModel.TripInvoiceRequest;
import com.app.freeroading.model.requestModel.TripStatusUpdateRequest;
import com.apps.freeroadingdriver.FreeRoadingApp;
import com.apps.freeroadingdriver.model.requestModel.AcceptRideRequest;
import com.apps.freeroadingdriver.model.requestModel.ActiveRideRequest;
import com.apps.freeroadingdriver.model.requestModel.AddRoadTripRequest;
import com.apps.freeroadingdriver.model.requestModel.BaseRequest;
import com.apps.freeroadingdriver.model.requestModel.BeginJourneyRequest;
import com.apps.freeroadingdriver.model.requestModel.CancelIndividualRideRequest;
import com.apps.freeroadingdriver.model.requestModel.CancelRequest;
import com.apps.freeroadingdriver.model.requestModel.CancelRoadTripRequest;
import com.apps.freeroadingdriver.model.requestModel.ChangeAvailableRequest;
import com.apps.freeroadingdriver.model.requestModel.ChangePassword;
import com.apps.freeroadingdriver.model.requestModel.DriverArrivedRequest;
import com.apps.freeroadingdriver.model.requestModel.DroppedPassengerRequest;
import com.apps.freeroadingdriver.model.requestModel.EditJourneyRequest;
import com.apps.freeroadingdriver.model.requestModel.EditProfileRequest;
import com.apps.freeroadingdriver.model.requestModel.FeedBackRequest;
import com.apps.freeroadingdriver.model.requestModel.LoginRequest;
import com.apps.freeroadingdriver.model.requestModel.MobileVerificationRequest;
import com.apps.freeroadingdriver.model.requestModel.ModelRequest;
import com.apps.freeroadingdriver.model.requestModel.RejectRideRequest;
import com.apps.freeroadingdriver.model.requestModel.ResetPasswordRequest;
import com.apps.freeroadingdriver.model.requestModel.RideHistoryDetailRequest;
import com.apps.freeroadingdriver.model.requestModel.RideHistoryRequest;
import com.apps.freeroadingdriver.model.requestModel.SaveVehicleRequest;
import com.apps.freeroadingdriver.model.requestModel.SignUpRequest;
import com.apps.freeroadingdriver.model.requestModel.StatisticsRequest;
import com.apps.freeroadingdriver.model.requestModel.TermsAboutRequest;
import com.apps.freeroadingdriver.model.requestModel.TripHistoryRequest;
import com.apps.freeroadingdriver.model.requestModel.TripJourneyStartRequest;
import com.apps.freeroadingdriver.model.requestModel.UpdateStripeRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.RequestBody;
public class HTTPOperationController {
    private static final String HEADER_USER_AGENT = "user-agent";
    private static final int _4KB = 4 * 1024;
    private final String TAG = HTTPOperationController.class.getSimpleName();
    private final String CONTENT_TYPE = "application/json";
    private final String ACCEPT = "application/json";
    private final String HEADER_TOKEN = "token";

    public static CZResponse mobileVerification(MobileVerificationRequest mobileVerificationRequest) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.MobileVerification(mobileVerificationRequest));
    }
    public static CZResponse signinUser(SignUpRequest signIn) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.SignUp(getSignInRequest(signIn)));
    }
    private static Map<String, RequestBody> getSignInRequest(SignUpRequest signInRequest) {
        Map<String, RequestBody> requestmap = new HashMap();
        requestmap.put("email", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getEmail()));
        requestmap.put("mobile", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getMobile()));
        requestmap.put("device_id", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getDevice_id()));
        requestmap.put("device_token", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getDevice_token()));
        requestmap.put("device_type", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getDevice_type()));
        requestmap.put("name", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getName()));
        requestmap.put("request_type", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getRequest_type()));
        requestmap.put("password", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getPassword()));
        requestmap.put("latitude", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getLatitude() + ""));
        requestmap.put("longitude", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getLongitude() + ""));
        requestmap.put("language", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getLanguage()));
        requestmap.put("debug_mode", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(signInRequest.getDebug_mode())));
        requestmap.put("confirm_password", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getConfirm_password()));
        requestmap.put("country_code", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getCountry_code()));
        if (signInRequest.getProfile_pic() != null) {
            File imageFile = new File(signInRequest.getProfile_pic());
            requestmap.put("profile_pic\"; filename=\"pp.png\" ", RequestBody.create(MediaType.parse("image*//**//*"), imageFile));
        }
        return requestmap;
    }

    public static CZResponse loginUser(LoginRequest userDetail) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.Login(userDetail));
    }

    public static CZResponse forgotUser(MobileVerificationRequest forgotRequest) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.generateOTP(forgotRequest));
    }

    public static CZResponse resetpassword(ResetPasswordRequest forgotRequest) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.resetRassword(forgotRequest));
    }

    public static CZResponse changeAvailable(ChangeAvailableRequest forgotRequest) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.updatestatus(forgotRequest));
    }

    public static CZResponse carDetails(BaseRequest logout) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.getcardetail(logout));
    }
    public static CZResponse getTripHistory(TripHistoryRequest request) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.getTripHistory(request));
    }
    public static CZResponse fetchProfile(BaseRequest signIn) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.getprofile(signIn));
    }

    public static CZResponse statistics(StatisticsRequest forgotRequest) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.statisticsdata(forgotRequest));
    }

    public static CZResponse editProfile(EditProfileRequest signIn) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.editprofile(getEditProfileRequest(signIn)));
    }

    public static CZResponse saveVehicle(SaveVehicleRequest saveVehicleRequest) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.saveVehicle(getVehicle(saveVehicleRequest)));
    }

    private static Map<String, RequestBody> getEditProfileRequest(EditProfileRequest signInRequest) {
        Map<String, RequestBody> requestmap = new HashMap();

        if (signInRequest.getName() != null) {
            requestmap.put("name", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getName()));
        }

        if (signInRequest.getMobile() != null) {
            requestmap.put("mobile", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getMobile()));
        }

        if (signInRequest.getEmail() != null) {
            requestmap.put("email", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getEmail()));
        }

        if (signInRequest.getCountry_code() != null) {
            requestmap.put("country_code", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getCountry_code()));
        }

        requestmap.put("device_type", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getDevice_type()));
        requestmap.put("language", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getLanguage()));
        requestmap.put("session_token", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getSession_token()));
        requestmap.put("home_addrs", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getHome_addrs()));
        requestmap.put("otp", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getOtp()));
        requestmap.put("class_license_held", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getClass_license_held()));
        requestmap.put("driving_experience", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getDriving_experience()));
        requestmap.put("facebook_url", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getFacebook_url()));
        requestmap.put("hobbies", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getHobbies()));
        requestmap.put("instagram_url", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getInstagram_url()));
        requestmap.put("interests", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getInterests()));
        requestmap.put("languages_spoken", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getLanguages_spoken()));
        requestmap.put("linkedin_url", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getLinkedin_url()));
        requestmap.put("music_preferences", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getMusic_preferences()));
        requestmap.put("pet_friendly", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getPet_friendly()));
        requestmap.put("smoking", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getSmoking()));
        requestmap.put("wheel_drive_type", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getWheel_drive_type()));
        //edt_driver_license_no.text.toString(), txt_exp_date.text.toString(),txt_issue_date.text.toString(),edt_state_issue.text.toString()
        requestmap.put("driver_license_no", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getDriver_license_no()));
        requestmap.put("license_expiry_date", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getLicense_expiry_date()));
        requestmap.put("license_issue_date", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getLicense_issue_date()));
        requestmap.put("state_issue", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getState_issue()));

        if (signInRequest.getProfile_pic() != null) {
            File imageFile = new File(signInRequest.getProfile_pic());
            requestmap.put("profile_pic\"; filename=\"pp.png\" ", RequestBody.create(MediaType.parse("image*//**//*"), imageFile));
        }

        if (signInRequest.getDriver_license_pic() != null) {
            File imageFile = new File(signInRequest.getDriver_license_pic());
            requestmap.put("driver_license_pic\"; filename=\"licens.png\" ", RequestBody.create(MediaType.parse("image*//**//*"), imageFile));
        }
        return requestmap;
    }

    private static Map<String, RequestBody> getVehicle(SaveVehicleRequest signInRequest) {
        Map<String, RequestBody> requestmap = new HashMap();
        requestmap.put("device_type", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getDevice_type()));
        requestmap.put("is_ac", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getIs_ac()));
        requestmap.put("is_hybrid_electric", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getIs_hybrid_electric()));
        requestmap.put("language", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getLanguage()));
        requestmap.put("license_plate_no", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getLicense_plate_no()));
        requestmap.put("no_of_door", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getNo_of_door()));
        requestmap.put("no_of_seat", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getNo_of_seat()));
        requestmap.put("road_tax_no", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getRoad_tax_no()));
        requestmap.put("session_token", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getSession_token()));
        requestmap.put("state", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getState()));
        requestmap.put("upload_cert_reg_exp_date", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getUpload_cert_reg_exp_date()));
        requestmap.put("vehicle_color", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getVehicle_color()));
        requestmap.put("vehicle_insurance_no", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getVehicle_insurance_no()));
        requestmap.put("vehicle_make_id", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getVehicle_make_id()));
        requestmap.put("vehicle_model_id", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getVehicle_model_id()));
        requestmap.put("vehicle_reg_no", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getVehicle_reg_no()));
        requestmap.put("vehicle_type_id", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getVehicle_type_id()));
        requestmap.put("vehicle_wifi", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getVehicle_wifi()));
        requestmap.put("vehicle_year", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getVehicle_year()));
        requestmap.put("liability_coverage_amount", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getLiability_coverage_amount()));
        requestmap.put("collision_coverage_amount", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getCollision_coverage_amount()));
        requestmap.put("comprehensive_coverage_amount", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getComprehensive_coverage_amount()));
        requestmap.put("personal_injury_coverage_amount", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getPersonal_injury_coverage_amount()));
        requestmap.put("uninsured_motorist_coverage_amount", RequestBody.create(MediaType.parse("text/plain"), signInRequest.getUninsured_motorist_coverage_amount()));

        if (signInRequest.getTaxi_front_view() != null) {
            File imageFile = new File(signInRequest.getTaxi_front_view());
            requestmap.put("taxi_front_view\"; filename=\"front.png\" ", RequestBody.create(MediaType.parse("image*//**//*"), imageFile));
        }

        if (signInRequest.getTaxi_back_view() != null) {
            File imageFile = new File(signInRequest.getTaxi_back_view());
            requestmap.put("taxi_back_view\"; filename=\"pp.png\" ", RequestBody.create(MediaType.parse("image*//**//*"), imageFile));
        }

        if (signInRequest.getUpload_motor_insurance_cert() != null) {
            File imageFile = new File(signInRequest.getUpload_motor_insurance_cert());
            requestmap.put("upload_motor_insurance_cert\"; filename=\"ins.png\" ", RequestBody.create(MediaType.parse("image*//**//*"), imageFile));
        }
        return requestmap;
    }

    public static CZResponse getHomedData(BaseRequest request) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.getHomeData(request));
    }

    public static CZResponse getRideHistoryDetail(RideHistoryDetailRequest request) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.getRideHistoryDetail(request));
    }

    public static CZResponse acceptRequest(AcceptRideRequest request) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.acceptRideRequest(request));
    }

    public static CZResponse makeRequest(BaseRequest request) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.makeRequest(request));
    }

    public static CZResponse modelRequest(ModelRequest request) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.modelRequest(request));
    }


    public static CZResponse rejectRequest(RejectRideRequest request) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.rejectRideRequest(request));
    }

    public static CZResponse driverArrivedRequest(DriverArrivedRequest request) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.driverArrivedRequest(request));
    }

    public static CZResponse beginJourneyRequest(BeginJourneyRequest request) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.beginJourneyRequest(request));
    }

    public static CZResponse droppedPassengerRequest(DroppedPassengerRequest request) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.droppedPassengerRequest(request));
    }

    public static CZResponse logout(BaseRequest logout) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.logout(logout));
    }

    public static CZResponse feedBackRequest(FeedBackRequest request) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.feedBackRequest(request));
    }

    public static CZResponse getRideHistory(RideHistoryRequest request) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.getRideHistory(request));
    }

    public static CZResponse changePassword(ChangePassword forgotRequest) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.changepassword(forgotRequest));
    }


    public static CZResponse pages(TermsAboutRequest logout) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.pages(logout));
    }

    public static CZResponse cancelRequest(CancelRequest request) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.cancelRideRequest(request));
    }

    public static CZResponse editJourneyRequest(EditJourneyRequest request) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.editJourneyRequest(request));
    }

    public static CZResponse updateStripeRequest(UpdateStripeRequest request) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.updateStripeRequest(request));
    }

    public static CZResponse requestForm(AddRoadTripRequest logout) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.addeditride(logout));
    }

    public static CZResponse activeRide(ActiveRideRequest logout) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.activeRide(logout));
    }

    public static CZResponse cancelRoadTrip(CancelRoadTripRequest logout) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.cancelfulltripbydriver(logout));
    }

    public static CZResponse tripStart(TripJourneyStartRequest logout) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.tripjourneystarted(logout));
    }

    public static CZResponse tripFinsih(TripFinishRequest logout) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.tripfinished(logout));
    }

    public static CZResponse cancelIndividual(CancelIndividualRideRequest logout) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.cancelIndividualTripByDriver(logout));
    }

    public static CZResponse tripStatusUpdate(TripStatusUpdateRequest signIn) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.tripstatusupdate(signIn));
    }

    public static CZResponse tripInvoice(TripInvoiceRequest signIn) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.tripinvoice(signIn));
    }

    public static CZResponse sendfeedbakc(InvoicePayTripAmout request) {
        FreeApiInterface apiInterface = FreeRoadingApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.sendtripfeedback(request));
    }

    public String readUrl(String mapsApiDirectionsUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(mapsApiDirectionsUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {

        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

/*









    public static CZResponse rideHistry(RideHistoryRequest logout) {
        TaxiApiInterface apiInterface = TaxiShakePassengerApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.rideHistory(logout));
    }


    public static CZResponse sendrequest(SendRequest request) {
        TaxiApiInterface apiInterface = TaxiShakePassengerApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.sendRequest(request));
    }

    public static CZResponse sendLaterRequest(SendLaterRequest request) {
        TaxiApiInterface apiInterface = TaxiShakePassengerApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.sendlaterrequest	(request));
    }

    public static CZResponse fareEstimation(FareEstimationRequest forgotRequest) {
        TaxiApiInterface apiInterface = TaxiShakePassengerApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.fareCalculator(forgotRequest));
    }
    public static CZResponse getAppoint(GetAppointmentRequest logout) {
        TaxiApiInterface apiInterface = TaxiShakePassengerApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.getAppointment(logout));
    }





    public static CZResponse chngePass(ChangePasswordRequest request) {
        TaxiApiInterface apiInterface = TaxiShakePassengerApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.changePassword(request));
    }

    public static CZResponse sendFeedBack(InvoiceRequest request) {
        TaxiApiInterface apiInterface = TaxiShakePassengerApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.sendfeedback(request));
    }

    public static CZResponse promoCode(PromoCodeRequest request) {
        TaxiApiInterface apiInterface = TaxiShakePassengerApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.checkpromocode(request));
    }

    public static CZResponse getPromoCodeList(BaseRequest pageRequest) {
        TaxiApiInterface apiInterface = TaxiShakePassengerApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.getPromocodeList(pageRequest));
    }
*/
    /*



     *//*Card API*//*

    public static CZResponse addCardRequest(AddCardRequest request) {
        GoGoApi apiInterface = GoGoBikeApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.addCard(request));
    }

    public static CZResponse deleteCardRequest(DeleteCardRequest request) {
        GoGoApi apiInterface = GoGoBikeApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.deleteCard(request));
    }

    public static CZResponse getCardList(BaseRequest request) {
        GoGoApi apiInterface = GoGoBikeApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.getCardList(request));
    }






    public static CZResponse changePassword(ForgotRequest forgotRequest) {
        //setLanguage(forgotRequest);
        GoGoApi apiInterface = GoGoBikeApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.changePassword(forgotRequest));
    }



    public static CZResponse verifyotp(ForgotRequest forgotRequest) {
        //setLanguage(forgotRequest);
        GoGoApi apiInterface = GoGoBikeApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.VerifyOtp(forgotRequest));
    }

    public static CZResponse updateDeviceToken(BaseRequest request) {
        GoGoApi apiInterface = GoGoBikeApp.getInstance().getCoreApi();
        return ConnectionUtil.execute(apiInterface.UpdateDeviceToken(request));
    }

   *//* private static void setDeviceType(BaseRequest request) {
        request.setDevice_type(AppConstant.DEVICE_TYPE);
    }

    private static void setDeviceId(BaseRequest request) {
        request.setDevice_id(AppConstant.DEVICE_ID);
    }

    private static void setDeviceToken(BaseRequest request) {
        request.setDevice_token(AppConstant.DEVICE_TOKEN);
    }

    private static void setLanguage(BaseRequest request) {
        request.setLanguage(AppConstant.APP_LANGUAGE);
    }

    private static void setLatLong(BaseRequest request) {
        Location location = LocationManagerWIthGps.getInstance().getLocation();
        if (location != null) {
            request.setLatitude(location.getLatitude());
            request.setLongitude(location.getLongitude());
        } else {
            request.setLatitude(0.0);
            request.setLongitude(0.0);
        }
    }*//*
     *//*
    private static void setCountryCode(BaseRequest request) {
        request.setCountry_code("91");
    }*//*

    public static CZResponse postMultiPartFileUpload(String urlString, String filePath) {

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1024 * 1024;
        File sourceFile = new File(filePath);
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "image/jpg");
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            // create a buffer of  maximum size
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            fileInputStream.close();
            dos.flush();
            dos.close();
            int statusCode = conn.getResponseCode();
            switch (statusCode) {
                case HttpURLConnection.HTTP_OK:
                    String response = new String(readFullyBytes(conn.getInputStream(), 2 * _4KB));
                    return new CZResponse(statusCode, response);
                default:
                    return new CZResponse(statusCode, "");
            }
        } catch (MalformedURLException ex) {
            LogManager.e("TAG", "error: " + ex.getMessage() + ex);
        } catch (Exception e) {
            LogManager.e("TAG", "Exception : " + e.getMessage() + e);
        }
        return null;
    }

    */

    /**
     * Read bytes from InputStream efficiently. All data will be read from
     * stream. This method return the bytes or null. This method will not close
     * the stream.
     *//*
    public static byte[] readFullyBytes(InputStream is, int blockSize) {
        byte[] bytes = null;
        if (is != null) {
            try {
                int readed = 0;
                byte[] buffer = new byte[blockSize];
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                while ((readed = is.read(buffer)) >= 0) {
                    bos.write(buffer, 0, readed);
                }
                bos.flush();
                bytes = bos.toByteArray();
            } catch (IOException e) {
                LogManager.e("TAG", " : readFullyBytes: " + e);
            }
        }
        return bytes;
    }*/


}
