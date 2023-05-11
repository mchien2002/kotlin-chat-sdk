package com.example.ae_chat_sdk.data.api

class ApiConstant {
    companion object {
        const val URL = "https://d03a-2001-ee0-4fc7-dce0-342e-fff5-29b4-c028.ngrok-free.app/api/v1/"
        const val MEDIA = "https://d03a-2001-ee0-4fc7-dce0-342e-fff5-29b4-c028.ngrok-free.app/"
        const val URL_IMAGE = MEDIA + "image/"
        const val ACCOUNT_POST_EMAIL = "register-by-email"
        const val VERIFY_OTP = "verifyOTP-email"
        const val UPLOAD_USER_AVATAR = "upload-user-avatar"
        const val SEARCH_USER = "search-user"
        const val USER_PROFILE = "user-profile"
        const val USER_ONLINE_STATUS = "user-online-status"
        const val GROUP_PROFILE_BY_MEMBER = "group-profile-by-member"
        const val UPDATE_USER = "update-profile-user"
    }
}
