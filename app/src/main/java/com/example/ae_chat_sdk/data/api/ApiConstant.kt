package com.example.ae_chat_sdk.data.api

class ApiConstant {
    companion object {
            const val URL = "https://62f7-2001-ee0-4fc7-dce0-9c64-429a-172-979e.ngrok-free.app/api/v1/"
            const val MEDIA = "https://62f7-2001-ee0-4fc7-dce0-9c64-429a-172-979e.ngrok-free.app/"
            const val URL_AUDIO = MEDIA + "audio/"
            const val URL_IMAGE = MEDIA + "image/"
            const val URL_VIDEO = MEDIA + "video/"
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
