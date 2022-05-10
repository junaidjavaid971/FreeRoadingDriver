package com.apps.freeroadingdriver.new_modelll


data class ResponseData(
        val driver_wallet_status: Int = 0,
        val profile: Profile = Profile(),
        val pub_chn: String = "",
        val ser_chn: String = "",
        val sessionData: List<Any> = listOf(),
        val session_token: String = ""
)