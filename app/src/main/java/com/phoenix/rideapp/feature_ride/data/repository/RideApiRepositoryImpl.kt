package com.phoenix.rideapp.feature_ride.data.repository

import com.phoenix.rideapp.feature_ride.data.api.RideApiService
import com.phoenix.rideapp.feature_ride.data.api.RideEstimateRequest
import com.phoenix.rideapp.feature_ride.domain.model.RideEstimate
import com.phoenix.rideapp.feature_ride.domain.model.ride_api.repository.RideApiRepository
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
}