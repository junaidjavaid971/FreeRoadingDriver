package com.apps.freeroadingdriver.network

/**
 * Created by Harshil on 2/22/2018.
 */
class URLConstant {
    companion object {
       // val BASE_URL: String="http://freeroading.onsisdev.info/"
       // val BASE_URL: String="https://www.drivefreeroading.com/admin/"
        //145b063256eb401b55eacd026fbc6947.png
        //https://www.approading.com/admin/public/media/customer/4743b227f4919621b9cbd9bcb9ea0cbb.png
        val BASE_URL: String="https://www.approading.com/admin/"
        val APP_SERVER_URL : String = BASE_URL+"driverapi/"
        val PASSENGER_IMAGE_URL : String = BASE_URL+"public/media/customer/"
        val DRIVER_IMAGE_URL : String = BASE_URL+"public/media/driver/"
    }
}