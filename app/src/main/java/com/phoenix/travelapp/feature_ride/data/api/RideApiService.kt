package com.phoenix.travelapp.feature_ride.data.api

import com.phoenix.travelapp.feature_ride.domain.model.RideEstimateValueResponse
import retrofit2.http.Body
import retrofit2.http.POST

const val RIDE_API_BASE_URL = "https://xd5zl5kk2yltomvw5fb37y3bm40vsyrx.lambda-url.sa-east-1.on.aws"

/**
 * Interface que define os endpoints da API de viagens
 */
interface RideApiService {

    // Retorna as opções de motoristas disponíveis para a viagem e os respectivos custos
    @POST("ride/estimate")
    suspend fun getRideOptions(
        @Body request: RideEstimateRequest
    ): RideEstimateValueResponse

}

/**
 * Propriedades necessárias para o cálculo da viagem
 *
 * @property customerId ID do cliente que está solicitando a viagem
 * @property originAddress Endereço de origem da viagem
 * @property destinationAddress Endereço de destino da viagem
 */
data class RideEstimateRequest(
    val customerId: String,
    val originAddress: String,
    val destinationAddress: String
)