package com.example.ae_chat_sdk.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DateTimeUtil {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getTimeFromDate(dateString: String): String {
        // val createdAtString = itemObject.lastMessage.createdAt.toString()
        val dateFormat = SimpleDateFormat("MMM d, yyyy, h:mm:ss a", Locale.ENGLISH)
        val createdAtDate = dateFormat.parse(dateString)
        val outputDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val formattedDateString = outputDateFormat.format(createdAtDate)

        val currentDate = LocalDate.now()
        val dateFormat1 = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formattedDate = currentDate.format(dateFormat1)

        if (formattedDateString.equals(formattedDate)) {
            // val inputDate = itemObject.lastMessage.createdAt.toString()
            val inputFormat = SimpleDateFormat("MMM d, yyyy, h:mm:ss a", Locale.US)
            val date = inputFormat.parse(dateString)
            val outputFormat = SimpleDateFormat("h:mm a", Locale.US)
            val time = outputFormat.format(date)
            return time
        } else {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val date = LocalDate.parse(formattedDateString, formatter)
            val newFormatter = DateTimeFormatter.ofPattern("dd MMM", Locale("vi", "VN"))
            val newDateString = date.format(newFormatter)
            return newDateString
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDate(dateString: String):String{
        val inputFormat = DateTimeFormatter.ofPattern("MMM dd, yyyy, hh:mm:ss a")
        val outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        val inputDateTime = LocalDateTime.parse("Apr 25, 2023, 12:37:47 AM", inputFormat)
        val outputDateTime = inputDateTime.format(outputFormat)
        return outputDateTime
    }
}
