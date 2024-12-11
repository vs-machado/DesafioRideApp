package com.phoenix.rideapp.feature_ride.domain.model.ride_api

import com.phoenix.rideapp.feature_ride.domain.model.common.Driver
import com.phoenix.rideapp.feature_ride.presentation.ride_prices_screen.RidePricesScreen

/**
 * Interface que define o repositório para acessar a API de serviços de corrida
 */
interface RideApiRepository {

    /**
     * Retorna as opções de motoristas disponíveis para a corrida e os respectivos custos
     *
     * @param customerId ID do cliente que está solicitando a corrida
     * @param originAddress Endereço de origem da corrida
     * @param destinationAddress Endereço de destino da corrida
     * @return Resultado contendo uma lista de opções de corrida disponíveis
     */
    suspend fun getRideEstimate(
        customerId: String,
        originAddress: String,
        destinationAddress: String
    ): Result<RideEstimate>

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
    suspend fun confirmRide(
        customerId: String,
        destination: String,
        distance: Int,
        driver: Driver,
        duration: String,
        origin: String,
        value: Double
    ): Result<ConfirmRideResponse>

    /**
     * Retorna o histórico de corridas para o id de usuário fornecido
     *
     * @param userId ID do cliente a ser consultado
     * @param driverId ID do motorista selecionado na [RidePricesScreen]
     *
     * @return [RideHistoryResponse] contendo o histórico de corridas do cliente
     */
    suspend fun getRideHistory(
        userId: String,
        driverId: Int
    ): Result<RideHistoryResponse>
}