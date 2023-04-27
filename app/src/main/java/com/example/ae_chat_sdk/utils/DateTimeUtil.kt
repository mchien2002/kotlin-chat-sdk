package com.example.ae_chat_sdk.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DateTimeUtil {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getTimeFromDate(date: Date?): String {
        // val createdAtString = itemObject.lastMessage.createdAt.toString()
//        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
//        val createdAtDate = dateFormat.parse(date)
//        val outputDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
//        val formattedDateString = outputDateFormat.format(createdAtDate)

//        val currentDate = LocalDate.now()
//        val dateFormat1 = DateTimeFormatter.ofPattern("dd/MM/yyyy")
//        val formattedDate = currentDate.format(dateFormat1)
//
//        if (formattedDateString.equals(formattedDate)) {
//            // val inputDate = itemObject.lastMessage.createdAt.toString()
//            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
//            val date = inputFormat.parse(dateString)
//            val outputFormat = SimpleDateFormat("h:mm a", Locale.US)
//            val time = outputFormat.format(date)
//            return time
//        } else {
//            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
//            val date = LocalDate.parse(formattedDateString, formatter)
//            val newFormatter = DateTimeFormatter.ofPattern("dd MMM", Locale("vi", "VN"))
//            val newDateString = date.format(newFormatter)

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
}
