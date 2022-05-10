package com.apps.freeroadingdriver.network

import com.app.freeroading.model.requestModel.InvoicePayTripAmout
import com.app.freeroading.model.requestModel.TripFinishRequest
import com.app.freeroading.model.requestModel.TripInvoiceRequest
import com.app.freeroading.model.requestModel.TripStatusUpdateRequest
import com.apps.freeroadingdriver.model.requestModel.*
import com.apps.freeroadingdriver.model.responseModel.BaseResponse
import okhttp3.RequestBody
import retrofit2.Call
import com.apps.freeroadingdriver.model.requestModel.FeedBackRequest
import com.apps.freeroadingdriver.newModel.ResponseModel
import retrofit2.http.*

/**
 * Created by Harshil on 2/22/2018.
 */
interface FreeApiInterface {
    @POST("signup")
    @Multipart
    fun SignUp(@PartMap signUp: Map<String, @JvmSuppressWildcards RequestBody>): Call<BaseResponse>


    @POST("signup")
    @Multipart
    fun SignUpTEMP(@Body signUp: SignUpRequest?): Call<ResponseModel>

    @POST("sendotp")
     fun MobileVerification(@Body mobileVerificationRequest: MobileVerificationRequest): Call<BaseResponse>

    @POST("login")
    abstract fun Login(@Body loginRequest: LoginRequest): Call<BaseResponse>

    @POST("forgotpassword")
    abstract fun generateOTP(@Body otp: MobileVerificationRequest): Call<BaseResponse>

    @POST("resetpassword")
    abstract fun resetRassword(@Body otp: ResetPasswordRequest): Call<BaseResponse>

    @POST("updatestatus")
    abstract fun updatestatus(@Body otp: ChangeAvailableRequest): Call<BaseResponse>

    @POST("getcardetail")
    abstract fun getcardetail(@Body otp: BaseRequest): Call<BaseResponse>

    @POST("statisticsdata")
    abstract fun statisticsdata(@Body otp: StatisticsRequest): Call<BaseResponse>

    @POST("gethomedata")
    abstract fun getHomeData(@Body logout: BaseRequest): Call<BaseResponse>

    @POST("editprofile")
    @Multipart
    fun editprofile(@PartMap edit: Map<String, @JvmSuppressWildcards RequestBody>): Call<BaseResponse>

    @POST("addeditcar")
    @Multipart
    fun saveVehicle(@PartMap edit: Map<String, @JvmSuppressWildcards RequestBody>): Call<BaseResponse>


    @POST("getprofile")
    abstract fun getprofile(@Body request: BaseRequest): Call<BaseResponse>


    @POST("acceptrequest")
    abstract fun acceptRideRequest(@Body request: AcceptRideRequest): Call<BaseResponse>

    @POST("getvehicletypeandmake")
    abstract fun makeRequest(@Body request: BaseRequest): Call<BaseResponse>

    @POST("getvehiclemodel")
    abstract fun modelRequest(@Body request: ModelRequest): Call<BaseResponse>


    @POST("getappointment")
    abstract fun getRideHistoryDetail(@Body request: RideHistoryDetailRequest): Call<BaseResponse>

    @POST("rejectrequest")
    abstract fun rejectRideRequest(@Body request: RejectRideRequest): Call<BaseResponse>

    @POST("updaterequest")
    abstract fun driverArrivedRequest(@Body request: DriverArrivedRequest): Call<BaseResponse>

    @POST("updaterequest")
    abstract fun beginJourneyRequest(@Body request: BeginJourneyRequest): Call<BaseResponse>

    @POST("passengerdrop")
    abstract fun droppedPassengerRequest(@Body request: DroppedPassengerRequest): Call<BaseResponse>

    @POST("logout")
    abstract fun logout(@Body logout: BaseRequest): Call<BaseResponse>

    @POST("sendfeedback")
    fun feedBackRequest(@Body request: FeedBackRequest): Call<BaseResponse>

    @POST("history")
    abstract fun getRideHistory(@Body request: RideHistoryRequest): Call<BaseResponse>

    @POST("changepassword")
    abstract fun changepassword(@Body request: ChangePassword): Call<BaseResponse>

    @POST("pages")
    abstract fun pages	(@Body request: TermsAboutRequest): Call<BaseResponse>

    @POST("cancelrequest")
    abstract fun cancelRideRequest(@Body request: CancelRequest): Call<BaseResponse>

    @POST("startstopride")
    abstract fun editJourneyRequest(@Body request: EditJourneyRequest): Call<BaseResponse>

    @POST("editstripeconnect")
    abstract fun updateStripeRequest(@Body request: UpdateStripeRequest): Call<BaseResponse>

    @POST("addeditride")
    abstract fun addeditride	(@Body request: AddRoadTripRequest): Call<BaseResponse>

    @POST("activeride")
    abstract fun activeRide	(@Body request: ActiveRideRequest): Call<BaseResponse>

    @POST("cancelfulltripbydriver")
    abstract fun cancelfulltripbydriver(@Body request: CancelRoadTripRequest): Call<BaseResponse>

    @POST("tripjourneystarted")
    abstract fun tripjourneystarted	(@Body request: TripJourneyStartRequest): Call<BaseResponse>

    @POST("tripstatusupdate")
    abstract fun tripstatusupdate(@Body request: TripStatusUpdateRequest): Call<BaseResponse>

    @POST("tripfinished")
    abstract fun tripfinished(@Body request: TripFinishRequest): Call<BaseResponse>

    @POST("cancelindividualtripbydriver")
    abstract fun cancelIndividualTripByDriver(@Body request: CancelIndividualRideRequest): Call<BaseResponse>

    @POST("tripinvoice")
    abstract fun tripinvoice(@Body request: TripInvoiceRequest): Call<BaseResponse>

    @POST("triphistory")
    abstract fun getTripHistory(@Body request: TripHistoryRequest): Call<BaseResponse>

    @POST("sendtripfeedback")
    abstract fun sendtripfeedback	(@Body request: InvoicePayTripAmout): Call<BaseResponse>


}