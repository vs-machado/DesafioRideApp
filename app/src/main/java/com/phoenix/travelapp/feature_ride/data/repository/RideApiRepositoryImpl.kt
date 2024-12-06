package com.phoenix.travelapp.feature_ride.data.repository

import com.phoenix.travelapp.feature_ride.data.api.RideApiService
import com.phoenix.travelapp.feature_ride.data.api.RideEstimateRequest
import com.phoenix.travelapp.feature_ride.domain.model.RideEstimateValueResponse
import com.phoenix.travelapp.feature_ride.domain.model.ride_api.repository.RideApiRepository
import javax.inject.Inject

class RideApiRepositoryImpl @Inject constructor (
    private val api: RideApiService
): RideApiRepository {

    override suspend fun getRideOptions(
        customerId: String,
        originAddress: String,
        destinationAddress: String
    ): Result<List<RideEstimateValueResponse.Option>> {

        return try {
            val request = RideEstimateRequest(customerId, originAddress, destinationAddress)
            val response = api.getRideOptions(request)
            Result.success(response.options)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}