package com.example.ae_chat_sdk.data.api

class ApiConstant {
    companion object {
        const val URL = "https://app-539ccd9b-9361-41a7-8adb-f7d64732641c.cleverapps.io/api/v1/"
        const val URL_IMAGE = "https://app-539ccd9b-9361-41a7-8adb-f7d64732641c.cleverapps.io/"
        const val URL_AVATAR = URL_IMAGE + "image/"
        const val ACCOUNT_POST_EMAIL = "register-by-email"
        const val VERIFY_OTP = "verifyOTP-email"
        const val UPLOAD_USER_AVATAR = "upload-user-avatar"
        const val SEARCH_USER = "search-user"
        const val USER_PROFILE = "user-profile"
        const val USER_ONLINE_STATUS = "user-online-status"
        const val GROUP_PROFILE_BY_MEMBER = "group-profile-by-member"
    }
}
