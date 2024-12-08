package com.phoenix.travelapp.feature_ride.presentation.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenix.travelapp.feature_ride.domain.model.RideEstimate
import com.phoenix.travelapp.feature_ride.domain.model.ride_api.repository.RideApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel que gerencia a MainScreen e RidePricesScreen.
 * Realiza o fetching dos preços da viagem e gerencia o estado da tela.
 * @property rideApiRepository Repositório que acessa a API de serviços de corrida
 */
@HiltViewModel
class RideEstimateSharedViewModel @Inject constructor(
    private val rideApiRepository: RideApiRepository
): ViewModel() {

    // Estado que gerencia o fetching do cálculo dos preços da viagem
    private val _priceCalculationState = MutableStateFlow<PriceCalculationState>(PriceCalculationState.Idle)
    val priceCalculationState: StateFlow<PriceCalculationState> = _priceCalculationState.asStateFlow()

    private lateinit var _rideEstimate: RideEstimate
    val rideEstimate: RideEstimate
        get() = _rideEstimate

    // Função que realiza o fetching das opções de viagem disponíveis
    fun fetchRidePrices(
        userId: String,
        origin: String,
        destination: String
    ) {
        viewModelScope.launch {
            _priceCalculationState.value = PriceCalculationState.Loading

            validateInputs(userId, origin, destination)?.let { error ->
                _priceCalculationState.value = PriceCalculationState.Error(error)
                return@launch
            }

            runCatching {
                rideApiRepository.getRideEstimate(
                    customerId = userId,
                    originAddress = origin,
                    destinationAddress = destination
                )
            }.fold(
                onSuccess = { rideEstimate ->
                    _priceCalculationState.value = PriceCalculationState.Success(rideEstimate)
                },
                onFailure = { exception ->
                    _priceCalculationState.value = PriceCalculationState.Error(exception.message ?: "Erro não identificado")
                }
            )
        }
    }

    fun saveRideEstimate(estimate: RideEstimate) {
        _rideEstimate = estimate
    }

    fun resetPriceCalculationState() {
        _priceCalculationState.value = PriceCalculationState.Idle

    }

    // Trata os casos em que o usuário deixa os campos de id/origem/destino vazios ou origem e destino iguais,
    // retornando uma mensagem de erro correspondente.
    private fun validateInputs(userId: String, origin: String, destination: String): String? {
        return when {
            origin.isBlank() -> "O endereço de origem não foi preenchido. Preencha o endereço e tente novamente."
            destination.isBlank() -> "O endereço de destino não foi preenchido. Preencha o endereço e tente novamente."
            userId.isBlank() -> "O id de usuário não foi preenchido. Preencha o id e tente novamente."
            origin == destination -> "Os endereços de destino de origem e destino não podem ser iguais. Preencha os endereços corretamente e tente novamente."
            else -> null
        }
    }
}

// Estado que gerencia o fetching do cálculo dos preços da viagem
sealed class PriceCalculationState {
    object Idle: PriceCalculationState()
    object Loading: PriceCalculationState()
    data class Success(val rideEstimate: Result<RideEstimate>): PriceCalculationState() // Substituir o resultado por uma data class com a resposta da API
    data class Error(val message: String): PriceCalculationState()
}