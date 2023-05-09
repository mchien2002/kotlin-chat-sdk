package com.example.ae_chat_sdk.utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs


class DateTimeUtil {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getTimeFromDate(date: Date?): String {

        // Lấy ngày tháng hiện tại
        val today = Calendar.getInstance()

        // Lấy ngày tháng của đối tượng Date
        val calendar = Calendar.getInstance()
        calendar.time = date

        if (calendar == today) {
            val outputFormat = SimpleDateFormat("h:mm a", Locale.US)
            val time = outputFormat.format(date)
            return time
        } else {
            val newFormatter = SimpleDateFormat("dd MMM", Locale("vi", "VN"))
            val newDateString = newFormatter.format(date)
            return newDateString
        }
        return " "
    }

    fun getElapsedTimeInWords(date: Date): String {
//        val date2 = "Mon May 09 11:09:00 GMT+07:00 2023"
//        val date9 = "Mon May 09 10:39:00 GMT+07:00 2023"
//        val date3 = "Mon May 09 07:30:00 GMT+07:00 2023"
//        val date4 = "Tue May 08 07:30:00 GMT+07:00 2023"
//        val date5 = "Tue May 01 07:30:00 GMT+07:00 2023"
//        val date6 = "Tue Jan 01 07:30:00 GMT+07:00 2023"
//        val date7 = "Tue Jan 01 07:30:00 GMT+07:00 2021"
//
//        val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
//        val datex = dateFormat.parse(date3)


        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"))
        val currentDate = calendar.time
        val elapsedMillis = currentDate.time - date.time
        val elapsedSeconds = elapsedMillis / 1000
        val elapsedMinutes = elapsedSeconds / 60
        val elapsedHours = elapsedMinutes / 60
        val elapsedDays = elapsedHours / 24
        val elapsedMonths = elapsedDays / 30
        val elapsedYears = elapsedMonths / 12

        return when {
            elapsedSeconds < 60 -> "Hoạt động $elapsedSeconds giây trước"
            elapsedMinutes < 60 -> "Hoạt động $elapsedMinutes phút trước"
            elapsedHours < 24 -> "Hoạt động $elapsedHours giờ trước"
            elapsedDays < 30 -> "Hoạt động $elapsedDays ngày trước"
            elapsedMonths < 12 -> "Hoạt động $elapsedMonths tháng trước"
            else -> "Hoạt động $elapsedYears năm trước"
        }
    }
}
