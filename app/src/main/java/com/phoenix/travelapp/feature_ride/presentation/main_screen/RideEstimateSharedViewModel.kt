package com.phoenix.travelapp.feature_ride.presentation.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenix.travelapp.feature_ride.domain.model.Option
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

    private lateinit var rideOptions: List<Option>

    // Função que realiza o fetching das opções de viagem disponíveis
    fun fetchRidePrices(
        userId: String,
        origin: String,
        destination: String
    ) {
        viewModelScope.launch {
            _priceCalculationState.value = PriceCalculationState.Loading

            runCatching {
                rideApiRepository.getRideOptions(
                    customerId = userId,
                    originAddress = origin,
                    destinationAddress = destination
                )
            }.fold(
                onSuccess = { rideOptions ->
                    _priceCalculationState.value = PriceCalculationState.Success(rideOptions)
                },
                onFailure = { exception ->
                    _priceCalculationState.value = PriceCalculationState.Error(exception.message ?: "Erro não identificado")
                }
            )
        }
    }

    fun saveRideOption(driverOptions: List<Option>) {
        rideOptions = driverOptions
    }

    fun getRideOption(): List<Option> {
        return rideOptions
    }

    fun resetPriceCalculationState() {
        _priceCalculationState.value = PriceCalculationState.Idle

    }
}

// Estado que gerencia o fetching do cálculo dos preços da viagem
sealed class PriceCalculationState {
    object Idle: PriceCalculationState()
    object Loading: PriceCalculationState()
    data class Success(val rideOptions: Result<List<Option>>): PriceCalculationState() // Substituir o resultado por uma data class com a resposta da API
    data class Error(val message: String): PriceCalculationState()
}