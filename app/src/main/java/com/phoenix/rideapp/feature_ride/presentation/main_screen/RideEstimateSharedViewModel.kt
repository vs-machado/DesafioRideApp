package com.phoenix.rideapp.feature_ride.presentation.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenix.rideapp.feature_ride.data.repository.RideApiRepositoryImpl
import com.phoenix.rideapp.feature_ride.domain.model.common.Driver
import com.phoenix.rideapp.feature_ride.domain.model.ride_api.ConfirmRideResponse
import com.phoenix.rideapp.feature_ride.domain.model.ride_api.RideApiRepository
import com.phoenix.rideapp.feature_ride.domain.model.ride_api.RideEstimate
import com.phoenix.rideapp.feature_ride.presentation.ride_history_screen.RideHistoryScreen
import com.phoenix.rideapp.feature_ride.presentation.ride_prices_screen.RidePricesScreen
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

    // Estado que gerencia o fetching da confirmação de viagem
    private val _rideConfirmationState = MutableStateFlow<RideConfirmationState>(RideConfirmationState.Idle)
    val rideConfirmationState: StateFlow<RideConfirmationState> = _rideConfirmationState.asStateFlow()

    private lateinit var _rideEstimate: RideEstimate
    val rideEstimate: RideEstimate
        get() = _rideEstimate

    var driverId = -1
        private set

    var customerId = ""
        private set

    var originAddress = ""
        private set

    var destinationAddress = ""
        private set

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

            saveCustomerData(
                id = userId,
                originAddress = origin,
                destinationAddress = destination
            )
            // Salva os dados para a eventual confirmação da viagem.
            customerId = userId
            originAddress = origin
            destinationAddress = destination

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

    // Função que realiza a confirmação da viagem
    fun confirmRide(
        userId: String,
        destination: String,
        distance: Int,
        driverId: Int,
        driverName: String,
        duration: String,
        origin: String,
        value: Double
    ) {
        viewModelScope.launch {
            _rideConfirmationState.value = RideConfirmationState.Loading

            val driver = Driver(
                id = driverId,
                name = driverName
            )

            runCatching {
                rideApiRepository.confirmRide(
                    customerId = userId,
                    destination = destination,
                    distance = distance,
                    driver = driver,
                    duration = duration,
                    origin = origin,
                    value = value
                )
            }.fold(
                onSuccess = { confirmRideResponse ->
                    saveSelectedDriver(driverId)
                    _rideConfirmationState.value = RideConfirmationState.Success(confirmRideResponse)
                },
                onFailure = { exception ->
                    _rideConfirmationState.value = RideConfirmationState.Error(exception.message ?: "Erro não identificado")
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

    /**
     *  Salva os dados do cliente.
     *
     *  Utilizado para salvar os dados do cliente antes de realizar
     *  a confirmação da viagem na classe [RideApiRepositoryImpl].
     *
     *  @param id ID do cliente
     *  @param originAddress Endereço de origem da viagem
     *  @param destinationAddress Endereço de destino da viagem
     *
     *  @see RideApiRepositoryImpl.confirmRide
     */
    private fun saveCustomerData(
        id: String,
        originAddress: String,
        destinationAddress: String
    ) {
        this.customerId = id
        this.originAddress = originAddress
        this.destinationAddress = destinationAddress
    }

    /**
     * Salva o motorista selecionado na [RidePricesScreen].
     *
     * A variável é utilizada para realizar a filtragem do histórico de corridas
     * na [RideHistoryScreen].
     */
    private fun saveSelectedDriver(driverId: Int) {
        this.driverId = driverId
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

// Estado que gerencia a confirmação da viagem
sealed class RideConfirmationState {
    object Idle: RideConfirmationState()
    object Loading: RideConfirmationState()
    data class Success(val confirmRideResponse: Result<ConfirmRideResponse>): RideConfirmationState()
    data class Error(val message: String): RideConfirmationState()
}

