package com.phoenix.rideapp.feature_ride.presentation.main_screen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.phoenix.rideapp.feature_ride.data.api.RideApiService
import com.phoenix.rideapp.feature_ride.data.repository.RideApiRepositoryImpl
import com.phoenix.rideapp.feature_ride.domain.model.ride_api.RideApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class RideEstimateSharedViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = StandardTestDispatcher()

    private lateinit var rideApi: RideApiService
    private lateinit var mockWebServer: MockWebServer
    private lateinit var viewModel: RideEstimateSharedViewModel
    private lateinit var rideApiRepository: RideApiRepository


    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)

        mockWebServer = MockWebServer()
        rideApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RideApiService::class.java)

        rideApiRepository = RideApiRepositoryImpl(rideApi)
        viewModel = RideEstimateSharedViewModel(rideApiRepository)
    }

    @After
    fun close() {
        mockWebServer.shutdown()
    }

    @Test
    fun `response code 200 retorna State success usando o metodo fetchRidePrices`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(getJsonContent("ride_estimate_success_response.json"))
        mockWebServer.enqueue(mockResponse)

        viewModel.fetchRidePrices(
            userId = "123",
            origin = "Av. Thomas Edison, 365 - Barra Funda, São Paulo - SP, 01140-000",
            destination = "Av. Paulista, 1538 - Bela Vista, São Paulo - SP, 01310-200"
        )

        advanceUntilIdle()

        // Checa o estado de priceCalculationState apos a chamada do metodo
        val state = viewModel.priceCalculationState.first { it is PriceCalculationState.Success }

        assertThat(state).isInstanceOf(PriceCalculationState.Success::class.java)
    }

    @Test
    fun `response code 400 retorna State error usando o metodo fetchRidePrices`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(400)
            .setBody(getJsonContent("ride_estimate_error_mesmo_endereco.json"))
        mockWebServer.enqueue(mockResponse)

        viewModel.fetchRidePrices(
            userId = "123",
            origin = "Av. Thomas Edison, 365 - Barra Funda, São Paulo - SP, 01140-000",
            destination = "Av. Paulista, 1538 - Bela Vista, São Paulo - SP, 01310-200"
        )

        advanceUntilIdle()

        val state = viewModel.priceCalculationState.first { it is PriceCalculationState.Error }

        assertThat(state).isInstanceOf(PriceCalculationState.Error::class.java)
    }

    @Test
    fun `retorna mensagem de erro quando o endereco de destino esta vazio usando o metodo fetchRidePrices`() = runTest {
        viewModel.fetchRidePrices(userId = "123", origin = "a", destination = "")

        advanceUntilIdle()

        val state = viewModel.priceCalculationState.value

        assertThat(state).isInstanceOf(PriceCalculationState.Error::class.java)
        assertThat((state as PriceCalculationState.Error).message).isEqualTo("O endereço de destino não foi preenchido. Preencha o endereço e tente novamente.")
    }

    @Test
    fun `retorna mensagem de erro quando o endereco de origem esta vazio usando o metodo fetchRidePrices`() = runTest {
        viewModel.fetchRidePrices(userId = "123", origin = "", destination = "a")

        advanceUntilIdle()

        val state = viewModel.priceCalculationState.value

        assertThat(state).isInstanceOf(PriceCalculationState.Error::class.java)
        assertThat((state as PriceCalculationState.Error).message).isEqualTo("O endereço de origem não foi preenchido. Preencha o endereço e tente novamente.")
    }

    @Test
    fun `retorna mensagem de erro quando o id de usuario esta vazio usando o metodo fetchRidePrices`() = runTest {
        viewModel.fetchRidePrices(userId = "", origin = "123", destination = "456")

        advanceUntilIdle()

        val state = viewModel.priceCalculationState.value

        assertThat(state).isInstanceOf(PriceCalculationState.Error::class.java)
        assertThat((state as PriceCalculationState.Error).message).isEqualTo("O ID de usuário não foi preenchido. Preencha o ID e tente novamente.")
    }

    @Test
    fun `retorna mensagem de erro quando endereco e destino sao iguais usando o metodo fetchRidePrices`() = runTest {
        viewModel.fetchRidePrices(userId = "55", origin = "123", destination = "123")

        advanceUntilIdle()

        val state = viewModel.priceCalculationState.value

        assertThat(state).isInstanceOf(PriceCalculationState.Error::class.java)
        assertThat((state as PriceCalculationState.Error).message).isEqualTo("Os endereços de destino de origem e destino não podem ser iguais. Preencha os endereços corretamente e tente novamente.")
    }

    @Test
    fun `response code 200 retorna rideConfirmationState success para o metodo confirmRide`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(getJsonContent("confirm_ride_success_response.json"))
        mockWebServer.enqueue(mockResponse)

        viewModel.confirmRide(
            userId = "123",
            destination = "Av. Paulista, 1538 - Bela Vista, São Paulo - SP, 01310-200",
            distance = 8001,
            driverId = 1,
            driverName = "Homer Simpson",
            duration = "1149",
            origin = "Av. Thomas Edison, 365 - Barra Funda, São Paulo - SP, 01140-000",
            value = 50.00
        )

        advanceUntilIdle()

        // Checa o estado de rideConfirmationState apos a chamada do metodo
        val state = viewModel.rideConfirmationState.first { it is RideConfirmationState.Success }

        assertThat(state).isInstanceOf(RideConfirmationState.Success::class.java)
    }

    @Test
    fun `response code 400 retorna rideConfirmationState error para o metodo confirmRide`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(400)
            .setBody(getJsonContent("confirm_ride_error_response_motorista.json"))
        mockWebServer.enqueue(mockResponse)

        viewModel.confirmRide(
            userId = "123",
            destination = "Av. Paulista, 1538 - Bela Vista, São Paulo - SP, 01310-200",
            distance = 8001,
            driverId = -1,
            driverName = "James Bonde",
            duration = "1149",
            origin = "Av. Thomas Edison, 365 - Barra Funda, São Paulo - SP, 01140-000",
            value = 50.00
        )

        advanceUntilIdle()

        // Checa o estado de rideConfirmationState apos a chamada do metodo
        val state = viewModel.rideConfirmationState.first { it is RideConfirmationState.Error }

        assertThat(state).isInstanceOf(RideConfirmationState.Error::class.java)

        // Exibe a mensagem de erro ao usuario
        assertThat((state as RideConfirmationState.Error).message).isEqualTo("Os dados fornecidos são inválidos.")
    }

    @Test
    fun `response code 404 retorna rideConfirmationState error para o metodo confirmRide`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(404)
            .setBody(getJsonContent("confirm_ride_error_response_motorista.json"))
        mockWebServer.enqueue(mockResponse)

        viewModel.confirmRide(
            userId = "123",
            destination = "Av. Paulista, 1538 - Bela Vista, São Paulo - SP, 01310-200",
            distance = 8001,
            driverId = 6,
            driverName = "James Bonde",
            duration = "1149",
            origin = "Av. Thomas Edison, 365 - Barra Funda, São Paulo - SP, 01140-000",
            value = 50.00
        )

        advanceUntilIdle()

        // Checa o estado de rideConfirmationState apos a chamada do metodo
        val state = viewModel.rideConfirmationState.first { it is RideConfirmationState.Error }

        assertThat(state).isInstanceOf(RideConfirmationState.Error::class.java)

        // Exibe a mensagem de erro ao usuario
        assertThat((state as RideConfirmationState.Error).message).isEqualTo("Falha ao selecionar motorista. Selecione outro motorista disponível.")
    }

    @Test
    fun `response code 406 retorna mensagem de quilometragem invalida para o metodo confirmRide`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(406)
            .setBody(getJsonContent("confirm_ride_error_response_distance.json"))
        mockWebServer.enqueue(mockResponse)

        viewModel.confirmRide(
            userId = "123",
            destination = "Av. Paulista, 1538 - Bela Vista, São Paulo - SP, 01310-200",
            distance = 8,
            driverId = 3,
            driverName = "James Bond",
            duration = "1149",
            origin = "Av. Thomas Edison, 365 - Barra Funda, São Paulo - SP, 01140-000",
            value = 50.00
        )

        advanceUntilIdle()

        // Checa o estado de rideConfirmationState apos a chamada do metodo
        val state = viewModel.rideConfirmationState.first { it is RideConfirmationState.Error }

        assertThat(state).isInstanceOf(RideConfirmationState.Error::class.java)

        // Exibe a mensagem de erro ao usuario
        assertThat((state as RideConfirmationState.Error).message).isEqualTo("Quilometragem inválida para o motorista selecionado.")
    }

    // Lê o arquivos Json do diretório resources
     private fun getJsonContent(fileName: String): String {
        val uri = this.javaClass.classLoader?.getResource(fileName)
        val file = File(uri?.path ?: throw IllegalArgumentException("File not found: $fileName"))
        return file.readText()
    }
}