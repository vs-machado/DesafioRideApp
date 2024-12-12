package com.phoenix.rideapp.feature_ride.domain.model.ride_api

import com.google.gson.annotations.SerializedName
import com.phoenix.rideapp.feature_ride.domain.model.common.Driver

// Retorna a resposta da confirmação da viagem
data class ConfirmRideResponse(
    val success: Boolean
)

/**
 *  Solicita a confirmação da viagem
 *  @property customerId ID do cliente que está solicitando a viagem
 *  @property destination Endereço do ponto de partida
 *  @property distance Distância total da viagem
 *  @property driver Dados do motorista
 *  @property duration Duração total da viagem
 *  @property origin Endereço do ponto de destino
 *  @property value Preço da viagem
 */
data class ConfirmRideRequest(
    @SerializedName("customer_id") val customerId: String,
    val destination: String,
    val distance: Double,
    val driver: Driver,
    val duration: String,
    val origin: String,
    val value: Double
)
