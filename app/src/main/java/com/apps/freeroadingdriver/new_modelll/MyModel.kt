package com.apps.freeroadingdriver.new_modelll

data class MyModel(
        val response_data: ResponseData = ResponseData(),
        val response_invalid: Int = 0,
        val response_key: Int = 0,
        val response_msg: String = "",
        val response_status: Int = 0
)