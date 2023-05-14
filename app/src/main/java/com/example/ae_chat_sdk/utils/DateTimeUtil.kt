package com.example.ae_chat_sdk.utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs


class DateTimeUtil {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getTimeFromDate(date: Date?): String {

        val today = Calendar.getInstance()
        val todayDay = today.get(Calendar.DAY_OF_MONTH)

        // Lấy ngày tháng của đối tượng Date
        val calendar = Calendar.getInstance()
        calendar.time = date
        val calendarDay = calendar.get(Calendar.DAY_OF_MONTH)

        if (calendarDay == todayDay) {
            val outputFormat = SimpleDateFormat("HH:mm", Locale.US)
            val time = outputFormat.format(date)
            return time
        } else {
            val newFormatter = SimpleDateFormat("dd MMM", Locale("vi", "VN"))
            val newDateString = newFormatter.format(date)
            return newDateString
        }
        return " "
    }

    fun getTimeFromDateMess(date: Date?): String {

        // Lấy ngày tháng hiện tại
        val today = Calendar.getInstance()
        val todayDay = today.get(Calendar.DAY_OF_MONTH)

        // Lấy ngày tháng của đối tượng Date
        val calendar = Calendar.getInstance()
        calendar.time = date
        val calendarDay = calendar.get(Calendar.DAY_OF_MONTH)

        if (calendarDay == todayDay) {
            val outputFormat = SimpleDateFormat("HH:mm", Locale.US)
            val time = outputFormat.format(date)
            return time
        } else {
            val newFormatter = SimpleDateFormat("HH:mm dd MMM", Locale("vi", "VN"))
            val newDateString = newFormatter.format(date)
            return newDateString
        }
        return " "
    }

    fun getElapsedTimeInWords(date: Date): String {
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
            elapsedSeconds < 60 -> "Vừa mới hoạt động"
            elapsedMinutes < 60 -> "Hoạt động $elapsedMinutes phút trước"
            elapsedHours < 24 -> "Hoạt động $elapsedHours giờ trước"
            elapsedDays < 30 -> "Hoạt động $elapsedDays ngày trước"
            elapsedMonths < 12 -> "Hoạt động $elapsedMonths tháng trước"
            else -> "Hoạt động $elapsedYears năm trước"
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateTimeDifference(timeString1: String, timeString2: String): Long {
        val formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
        val time1 = LocalDateTime.parse(timeString1, formatter)
        val time2 = LocalDateTime.parse(timeString2, formatter)

        val duration = Duration.between(time1, time2)
        val minutesDifference = duration.toMinutes()

        return minutesDifference
    }
}
