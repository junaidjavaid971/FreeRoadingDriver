package com.apps.freeroadingdriver.new_network

import com.apps.freeroadingdriver.model.requestModel.SignUpRequest
import com.apps.freeroadingdriver.newModel.MyPojo
import com.apps.freeroadingdriver.newModel.ResponseModel
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by Dnyaneshwar Dalvi on 22/11/17.
 */

interface ApiInterface {
    @Headers("Content-Type: application/json")
    @POST("signup")
    //@Multipart
    fun SignUpTEMP(@Body signUp: SignUpRequest?): Call<JsonElement>
}