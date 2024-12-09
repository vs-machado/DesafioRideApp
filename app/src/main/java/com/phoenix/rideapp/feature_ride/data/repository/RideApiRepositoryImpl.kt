package com.phoenix.rideapp.feature_ride.data.repository

import com.phoenix.rideapp.feature_ride.data.api.RideApiService
import com.phoenix.rideapp.feature_ride.data.api.RideEstimateRequest
import com.phoenix.rideapp.feature_ride.domain.model.ride_api.ConfirmRideRequest
import com.phoenix.rideapp.feature_ride.domain.model.ride_api.ConfirmRideResponse
import com.phoenix.rideapp.feature_ride.domain.model.ride_api.Driver
import com.phoenix.rideapp.feature_ride.domain.model.ride_api.RideEstimate
import com.phoenix.rideapp.feature_ride.domain.model.ride_api.RideApiRepository
import javax.inject.Inject

/**
 * Implementação do repositório para acessar a API de serviços de corrida
 */
class RideApiRepositoryImpl @Inject constructor (
    private val api: RideApiService
): RideApiRepository {

    // Retorna as opções de motoristas disponíveis para a viagem e os respectivos custos
    override suspend fun getRideEstimate(
        customerId: String,
        originAddress: String,
        destinationAddress: String
    ): Result<RideEstimate> {

        return try {
            val request = RideEstimateRequest(customerId, originAddress, destinationAddress)
            val response = api.getRideEstimate(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Solicita a confirmação da corrida
    override suspend fun confirmRide(
        customerId: String,
        destination: String,
        distance: Int,
        driver: Driver,
        duration: String,
        origin: String,
        value: Double
    ): Result<ConfirmRideResponse> {

        return try {
            val request = ConfirmRideRequest(
                customerId = customerId,
                destination = destination,
                distance = distance,
                driver = driver,
                duration = duration,
                origin = origin,
                value = value
            )
            val response = api.confirmRide(request)

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}