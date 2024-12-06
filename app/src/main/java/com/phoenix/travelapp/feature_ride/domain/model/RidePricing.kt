package com.phoenix.travelapp.feature_ride.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class RideEstimateValueResponse(
    val origin: Location,
    val destination: Location,
    val distance: Double,
    val duration: String,
    val options: List<Option>
    // val routeResponse: Map<String, Any>
) {
    @Serializable
    data class Location(
        val latitude: Double,
        val longitude: Double
    )

    @Serializable
    data class Option(
        val id: Int,
        val name: String,
        val description: String,
        val vehicle: String,
        val review: Review
    )

    @Serializable
    data class Review(
        val rating: Double,
        val comment: String
    )
}

@Serializable
data class ErrorResponse(
    val error_code: String,
    val error_description: String
)