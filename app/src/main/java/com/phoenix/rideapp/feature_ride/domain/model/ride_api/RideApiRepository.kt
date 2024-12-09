package com.phoenix.rideapp.feature_ride.domain.model.ride_api

/**
 * Interface que define o repositório para acessar a API de serviços de corrida
 */
interface RideApiRepository {

    /**
     * Retorna as opções de motoristas disponíveis para a viagem e os respectivos custos
     *
     * @param customerId ID do cliente que está solicitando a viagem
     * @param originAddress Endereço de origem da viagem
     * @param destinationAddress Endereço de destino da viagem
     * @return Resultado contendo uma lista de opções de viagem disponíveis
     */
    suspend fun getRideEstimate(
        customerId: String,
        originAddress: String,
        destinationAddress: String
    ): Result<RideEstimate>

    /**
     * Solicita a confirmação da viagem.
     *
     * @param customerId ID do cliente que está solicitando a viagem
     * @param destination Endereço do ponto de chegada da viagem
     * @param distance Distância total da viagem
     * @param driver Dados do motorista
     * @param duration Duração total da viagem
     * @param origin Endereço do ponto de partida da viagem
     * @param value Preço da viagem
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
}