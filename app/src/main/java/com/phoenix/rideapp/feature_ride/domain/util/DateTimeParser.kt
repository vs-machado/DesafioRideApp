package com.phoenix.rideapp.feature_ride.domain.util

import android.icu.text.SimpleDateFormat
import java.util.Locale

fun parseDateTime(dateTime: String): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault())
    val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(dateTime)
    return formatter.format(date)
}