package com.phoenix.travelapp.feature_ride.data.repository

import com.phoenix.travelapp.feature_ride.data.api.RideApiService
import com.phoenix.travelapp.feature_ride.data.api.RideEstimateRequest
import com.phoenix.travelapp.feature_ride.domain.model.Option
import com.phoenix.travelapp.feature_ride.domain.model.ride_api.repository.RideApiRepository
import javax.inject.Inject

/**
 * Implementação do repositório para acessar a API de serviços de corrida
 */
class RideApiRepositoryImpl @Inject constructor (
    private val api: RideApiService
): RideApiRepository {

    // Retorna as opções de motoristas disponíveis para a viagem e os respectivos custos
    override suspend fun getRideOptions(
        customerId: String,
        originAddress: String,
        destinationAddress: String
    ): Result<List<Option>> {

        return try {
            val request = RideEstimateRequest(customerId, originAddress, destinationAddress)
            val response = api.getRideOptions(request)
            Result.success(response.options)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}