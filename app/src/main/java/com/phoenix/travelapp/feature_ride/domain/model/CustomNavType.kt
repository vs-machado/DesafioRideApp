package com.phoenix.travelapp.feature_ride.domain.model

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.phoenix.travelapp.feature_ride.domain.model.Option

object CustomNavType {

    val RideOptionsType = object : NavType<List<Option>>(
        isNullableAllowed = false
    ){
        override fun get(bundle: Bundle, key: String): List<Option>? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): List<Option> {
           return Json.decodeFromString(Uri.decode(value))
        }

        override fun put(
            bundle: Bundle,
            key: String,
            value: List<Option>
        ) {
            bundle.putString(key, Json.encodeToString(value))
        }

        override fun serializeAsValue(value: List<Option>): String {
            return Uri.encode(Json.encodeToString(value))
        }
    }
}