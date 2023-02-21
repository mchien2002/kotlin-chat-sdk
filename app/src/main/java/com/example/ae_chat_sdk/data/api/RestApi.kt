package com.example.ae_chat_sdk.data.api

class RestApi {
    val TIME_OUT = 10000
    val ENABLE_LOG = true
    val ACCESS_TOKEN_HEADER = "TC-Token"
    val LANGUAGE = "Accept-Language"
    val TC_TOKEN = "TC-Token"
    private var baseUrl: String = ApiConstant.BASE_URL
    val header = HashMap<String, Any>().put("Content-Type", "application/json")
    fun init(token: String) {
        setToken(token)
    }

    private fun setToken(token: String) {}

    private fun setHeader(key: String, value: String) {}

    private fun removeHeader(key: String) {}

    private fun setLanguage(language: String) {}

    private fun clearToken() {}
}
