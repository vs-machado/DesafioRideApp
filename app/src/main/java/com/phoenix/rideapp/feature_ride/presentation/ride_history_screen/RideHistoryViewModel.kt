package com.phoenix.rideapp.feature_ride.presentation.ride_history_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenix.rideapp.core.helpers.LocationHelper
import com.phoenix.rideapp.feature_ride.domain.model.ride_api.RideApiRepository
import com.phoenix.rideapp.feature_ride.domain.model.ride_api.RideHistoryResponse
import com.phoenix.rideapp.feature_ride.presentation.ride_prices_screen.RidePricesScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RideHistoryViewModel @Inject constructor(
    private val rideApiRepository: RideApiRepository,
    private val locationHelper: LocationHelper
): ViewModel() {

    // Estado que gerencia o fetching do histórico de viagens
    private val _rideHistoryState = MutableStateFlow<RideHistoryState>(RideHistoryState.Idle)
    val rideHistoryState: StateFlow<RideHistoryState> = _rideHistoryState.asStateFlow()

    /**
     * Realiza o fetching do histórico de corridas de um usuário
     *
     * @param userId ID do usuário a ser consultado
     * @param driverId ID do motorista selecionado na [RidePricesScreen]
     */
    fun fetchRaceHistory(
        userId: String,
        driverId: Int
    ) {
        viewModelScope.launch {
            _rideHistoryState.value = RideHistoryState.Loading

            runCatching {
                rideApiRepository.getRideHistory(
                    userId = userId,
                    driverId = driverId
                )
            }.fold(
                onSuccess = { raceHistory ->
                    if(raceHistory.isSuccess) {
                        _rideHistoryState.value = RideHistoryState.Success(raceHistory)
                    } else {
                        _rideHistoryState.value = RideHistoryState.Error(
                            raceHistory.exceptionOrNull()?.message ?: locationHelper.getErrorUnknown()
                        )
                    }
                },
                onFailure = { error ->
                    _rideHistoryState.value = RideHistoryState.Error(error.message ?: locationHelper.getErrorFetchHistory())
                }
            )
        }
    }

    fun resetRideHistoryState() {
        _rideHistoryState.value = RideHistoryState.Idle
    }

    fun setError(message: String) {
        _rideHistoryState.value = RideHistoryState.Error(message)
    }

}

// Estado que gerencia o fetching do histórico de viagens do usuário consultado
sealed class RideHistoryState {
    object Idle: RideHistoryState()
    object Loading: RideHistoryState()
    data class Success(val history: Result<RideHistoryResponse>): RideHistoryState()
    data class Error(val message: String): RideHistoryState()
}