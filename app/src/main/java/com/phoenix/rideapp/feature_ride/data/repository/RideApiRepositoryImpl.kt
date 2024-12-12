package com.phoenix.rideapp.feature_ride.data.repository

import com.phoenix.rideapp.core.helpers.LocationHelper
import com.phoenix.rideapp.feature_ride.data.api.RideApiService
import com.phoenix.rideapp.feature_ride.data.api.RideEstimateRequest
import com.phoenix.rideapp.feature_ride.domain.model.common.Driver
import com.phoenix.rideapp.feature_ride.domain.model.ride_api.ConfirmRideRequest
import com.phoenix.rideapp.feature_ride.domain.model.ride_api.ConfirmRideResponse
import com.phoenix.rideapp.feature_ride.domain.model.ride_api.RideApiRepository
import com.phoenix.rideapp.feature_ride.domain.model.ride_api.RideEstimate
import com.phoenix.rideapp.feature_ride.domain.model.ride_api.RideHistoryResponse
import com.phoenix.rideapp.feature_ride.presentation.ride_prices_screen.RidePricesScreen
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Implementação do repositório para acessar a API de serviços de corrida.
 * LocationHelper é utilizada para abstrair o uso do context no metodo getString
 * para não prejudicar a testabilidade.
 *
 * @see [RideApiRepository]
 *
 * @property api Api de solicitação de corridas
 * @property locationHelper Interface que abstrai o uso do context
 */
class RideApiRepositoryImpl @Inject constructor (
    private val api: RideApiService,
    private val locationHelper: LocationHelper
): RideApiRepository {

    /**
     *  Retorna as opções de motoristas disponíveis para a corrida e os respectivos custos
     * 
     *  @param customerId ID do cliente que está solicitando a corrida
     *  @param originAddress Endereço de origem da corrida
     *  @param destinationAddress Endereço de destino da corrida
     *  
     *  @return Coordenadas da corrida e opções de motorista
     */
    override suspend fun getRideEstimate(
        customerId: String,
        originAddress: String,
        destinationAddress: String
    ): Result<RideEstimate> {

        return try {
            val request = RideEstimateRequest(customerId, originAddress, destinationAddress)
            val response = api.getRideEstimate(request)

            if(response.isSuccessful) {
                val responseBody = response.body()
                if(responseBody != null) {
                    Result.success(responseBody)
                } else {
                    Result.failure(Exception(locationHelper.getErrorRideRequestFailed()))
                }
            } else {
                Result.failure(Exception(locationHelper.getErrorUnexpected()))
            }
        } catch (e: IOException) {
            Result.failure(Exception(locationHelper.getErrorNoInternet()))
        } catch (e: HttpException) {
            val errorMessage = when(e.code()){
                400 -> locationHelper.getErrorInvalidData()
                500 -> locationHelper.getErrorServer()
                else -> locationHelper.getErrorContactSupport()
            }
            Result.failure(Exception(errorMessage))
        }
    }

    /**
     * Solicita a confirmação da corrida.
     *
     * @param customerId ID do cliente que está solicitando a corrida
     * @param destination Endereço do ponto de chegada da corrida
     * @param distance Distância total da corrida
     * @param driver Dados do motorista
     * @param duration Duração total da corrida
     * @param origin Endereço do ponto de partida da corrida
     * @param value Preço da corrida
     *
     * @return Retorna se a confirmação foi bem sucedida ou não
     */
    override suspend fun confirmRide(
        customerId: String,
        destination: String,
        distance: Double,
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
            val responseBody = response.body()

            if(responseBody != null) {
                Result.success(responseBody)
            } else {
                val errorMessage = when(response.code()) {
                    400 ->  locationHelper.getErrorInvalidProvidedData()
                    404 ->  locationHelper.getErrorDriverSelectionFailed()
                    406 -> locationHelper.getErrorInvalidMileage()
                    else -> locationHelper.getErrorUnexpected()
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: IOException) {
            Result.failure(Exception(locationHelper.getErrorNoInternet()))
        }
    }

    /**
     * Retorna o histórico de corridas para o id de usuário fornecido
     *
     * @param userId ID do cliente a ser consultado
     * @param driverId ID do motorista selecionado na [RidePricesScreen]
     *
     * @return [RideHistoryResponse] contendo o histórico de corridas do cliente
     */
    override suspend fun getRideHistory(
        userId: String,
        driverId: Int
    ): Result<RideHistoryResponse> {

        return try {
            val response = api.getRideHistory(userId, driverId)
            val responseBody = response.body()

            if(responseBody != null) {
                Result.success(responseBody)
            } else {
                val errorMessage = when(response.code()){
                    400 ->locationHelper.getErrorInvalidDriver()
                    404 ->  locationHelper.getErrorNoRidesFound()
                    500 ->  locationHelper.getErrorServer()
                    else -> locationHelper.getErrorContactSupport()
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: IOException) {
            Result.failure(Exception(locationHelper.getErrorNoInternet()))
        }
    }
}