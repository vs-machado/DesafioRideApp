package com.phoenix.travelapp.feature_ride.presentation.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenix.travelapp.feature_ride.domain.model.RideEstimateValueResponse
import com.phoenix.travelapp.feature_ride.domain.model.ride_api.repository.RideApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainScreenViewModel @Inject constructor(
    private val rideApiRepository: RideApiRepository
): ViewModel() {

    // Estado que gerencia o fetching do cálculo dos preços da viagem
    private val _priceCalculationState = MutableStateFlow<PriceCalculationState>(PriceCalculationState.Idle)
    val priceCalculationState: StateFlow<PriceCalculationState> = _priceCalculationState.asStateFlow()

    fun fetchRidePrices(
        customerId: String,
        originAddress: String,
        destinationAddress: String
    ) {
        viewModelScope.launch {
            _priceCalculationState.value = PriceCalculationState.Loading

            runCatching {
                rideApiRepository.getRideOptions(
                    customerId = customerId,
                    originAddress = originAddress,
                    destinationAddress = destinationAddress
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
}

sealed class PriceCalculationState {
    object Idle: PriceCalculationState()
    object Loading: PriceCalculationState()
    data class Success(val rideOptions: Result<List<RideEstimateValueResponse.Option>>): PriceCalculationState() // Substituir o resultado por uma data class com a resposta da API
    data class Error(val message: String): PriceCalculationState()
}