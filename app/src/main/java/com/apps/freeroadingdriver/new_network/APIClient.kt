package com.apps.freeroadingdriver.new_network

import android.graphics.Point
import com.apps.freeroadingdriver.model.dataModel.Profile
import com.apps.freeroadingdriver.network.URLConstant
import com.apps.freeroadingdriver.newModel.ResponseModel
import com.apps.freeroadingdriver.newModel.Response_data
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Dnyaneshwar Dalvi on 21/11/17.
 */
class APIClient {

    companion object {

        val baseURL: String = URLConstant.APP_SERVER_URL
        var retofit: Retrofit? = null


//        var gson = GsonBuilder()
//        .registerTypeAdapter(String::class.java, LongTypeAdapter())
//        .create()

        val client: Retrofit
            get() {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                val client = OkHttpClient.Builder()
                        .connectTimeout(90, TimeUnit.SECONDS)
                        .readTimeout(90, TimeUnit.SECONDS)
                        .addInterceptor(interceptor)
                        .build()

                if (retofit == null) {
                    retofit = Retrofit.Builder()
                            .baseUrl(baseURL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(client)
                            .build()
                }
                return retofit!!
            }
    }

}