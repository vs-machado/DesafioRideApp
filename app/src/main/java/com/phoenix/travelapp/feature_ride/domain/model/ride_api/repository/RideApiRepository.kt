package com.phoenix.travelapp.feature_ride.domain.model.ride_api.repository

import com.phoenix.travelapp.feature_ride.domain.model.Option

/**
 * Interface que define o repositório para acessar a API de serviços de corrida
 */
interface RideApiRepository {

    /**
     * Retorna as opções de motoristas disponíveis para a viagem e os respectivos custos
     * @param customerId ID do cliente que está solicitando a viagem
     * @param originAddress Endereço de origem da viagem
     * @param destinationAddress Endereço de destino da viagem
     * @return Resultado contendo uma lista de opções de viagem disponíveis
     */
    suspend fun getRideOptions(
        customerId: String,
        originAddress: String,
        destinationAddress: String
    ): Result<List<Option>>
}