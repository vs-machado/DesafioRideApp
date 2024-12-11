package com.phoenix.rideapp.feature_ride.domain.model.ride_api

import com.google.gson.annotations.SerializedName
import com.phoenix.rideapp.feature_ride.domain.model.common.Driver
import com.phoenix.rideapp.feature_ride.data.api.RideApiService

/**
 * Resposta da requisição de histórico de viagens da [RideApiService]
 *
 * @property customerId ID do cliente a ser consultado
 * @property rides Lista de viagens realizadas pelo cliente consultado
 */
data class RideHistoryResponse(
    @SerializedName("customer_id") val customerId: String,
    val rides: List<Ride>
)

/**
 * Classe com os dados de uma viagem
 *
 * @property date Data da viagem
 * @property destination Endereço de destino da viagem
 * @property distance Distância da viagem em quilômetros
 * @property driver Dados do motorista (Id e nome)
 * @property duration Duração da viagem
 * @property id ID da viagem
 * @property origin Endereço de origem da viagem
 * @property value Valor da viagem
 */
data class Ride(
    val date: String,
    val destination: String,
    val distance: Double,
    val driver: Driver,
    val duration: String,
    val id: Int,
    val origin: String,
    val value: Double
)
