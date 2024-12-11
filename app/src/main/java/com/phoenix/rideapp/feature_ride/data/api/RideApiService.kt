package com.phoenix.rideapp.feature_ride.data.api

import com.google.gson.annotations.SerializedName
import com.phoenix.rideapp.feature_ride.domain.model.ride_api.ConfirmRideRequest
import com.phoenix.rideapp.feature_ride.domain.model.ride_api.ConfirmRideResponse
import com.phoenix.rideapp.feature_ride.domain.model.ride_api.RideHistoryResponse
import com.phoenix.rideapp.feature_ride.domain.model.ride_api.RideEstimate
import com.phoenix.rideapp.feature_ride.presentation.ride_prices_screen.RidePricesScreen
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

const val RIDE_API_BASE_URL = "https://xd5zl5kk2yltomvw5fb37y3bm40vsyrx.lambda-url.sa-east-1.on.aws"

/**
 * Interface que define os endpoints da API de viagens
 */
interface RideApiService {

    // Retorna os dados da viagem, as opções de motoristas disponíveis e os respectivos custos
    @POST("ride/estimate")
    suspend fun getRideEstimate(
        @Body request: RideEstimateRequest
    ): Response<RideEstimate>

    @PATCH("ride/confirm")
    suspend fun confirmRide(
        @Body request: ConfirmRideRequest
    ): ConfirmRideResponse

    /**
     * Retorna o histórico de corridas para o id de usuário fornecido
     * e para o motorista selecionado na [RidePricesScreen]
     *
     * @param userId ID do cliente a ser consultado
     * @param driverId ID do motorista selecionado na [RidePricesScreen]
     * @return [RideHistoryResponse] contendo o histórico de corridas do cliente
     */
    @GET("ride/{customer_id}")
    suspend fun getRideHistory(
       @Path("customer_id") userId: String,
       @Query("driver_id") driverId: Int
    ): Response<RideHistoryResponse>
}

/**
 * Propriedades necessárias para o cálculo da viagem
 *
 * @property customerId ID do cliente que está solicitando a viagem
 * @property originAddress Endereço de origem da viagem
 * @property destinationAddress Endereço de destino da viagem
 */
data class RideEstimateRequest(
    @SerializedName("customer_id") val customerId: String,
    @SerializedName("origin") val originAddress: String,
    @SerializedName("destination") val destinationAddress: String
)


