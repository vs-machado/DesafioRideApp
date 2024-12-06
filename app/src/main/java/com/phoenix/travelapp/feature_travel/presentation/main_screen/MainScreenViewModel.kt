package com.phoenix.travelapp.feature_travel.presentation.main_screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainScreenViewModel: ViewModel() {

    // Estado que gerencia o fetching do cálculo dos preços da viagem
    private val _priceCalculationState = MutableStateFlow<PriceCalculationState>(PriceCalculationState.Idle)
    val priceCalculationState: StateFlow<PriceCalculationState> = _priceCalculationState.asStateFlow()

    // Realizar a API call aqui
    fun fetchTravelPrices() {}
}

sealed class PriceCalculationState {
    object Idle: PriceCalculationState()
    object Loading: PriceCalculationState()
    data class Success(val price: Double): PriceCalculationState() // Substituir o resultado por uma data class com a resposta da API
    data class Error(val message: String): PriceCalculationState()
}