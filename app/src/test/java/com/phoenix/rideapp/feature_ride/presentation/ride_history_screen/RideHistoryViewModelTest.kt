package com.phoenix.rideapp.feature_ride.presentation.ride_history_screen

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
class RideHistoryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = StandardTestDispatcher()

    private lateinit var rideApi: RideApiService
    private lateinit var mockWebServer: MockWebServer
    private lateinit var viewModel: RideHistoryViewModel
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
        viewModel = RideHistoryViewModel(rideApiRepository)
    }


    @After
    fun close() {
        mockWebServer.shutdown()
    }

    @Test
    fun `response code 200 retorna State success usando o metodo getHideHistory`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(getJsonContent("ride_estimate_success_response.json"))
        mockWebServer.enqueue(mockResponse)

        viewModel.fetchRaceHistory(
            userId = "CT01",
            driverId = 1
        )

        advanceUntilIdle()

        // Checa o estado de rideHistoryState apos a chamada do metodo
        val state = viewModel.rideHistoryState.first { it is RideHistoryState.Success }

        assertThat(state).isInstanceOf(RideHistoryState.Success::class.java)
    }

    @Test
    fun `response code 400 retorna State error e mensagem motorista invalido usando o metodo getHideHistory`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(400)
            .setBody(getJsonContent("ride_history_error_response_motorista.json"))
        mockWebServer.enqueue(mockResponse)

        viewModel.fetchRaceHistory(
            userId = "CT01",
            driverId = 5
        )

        advanceUntilIdle()

        // Checa o estado de rideHistoryState apos a chamada do metodo
        val state = viewModel.rideHistoryState.first { it is RideHistoryState.Error }


        assertThat(state).isInstanceOf(RideHistoryState.Error::class.java)

        // Exibe a mensagem de erro ao usuario
        assertThat((state as RideHistoryState.Error).message).isEqualTo("Motorista inválido. Corrija o nome e tente novamente.")
    }

    @Test
    fun `response code 404 retorna State error e mensagem nenhuma corrida encontrada usando o metodo getHideHistory`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(404)
            .setBody(getJsonContent("ride_history_error_response_motorista.json"))
        mockWebServer.enqueue(mockResponse)

        viewModel.fetchRaceHistory(
            userId = "CT0",
            driverId = 1
        )

        advanceUntilIdle()

        // Checa o estado de rideHistoryState apos a chamada do metodo
        val state = viewModel.rideHistoryState.first { it is RideHistoryState.Error }

        assertThat(state).isInstanceOf(RideHistoryState.Error::class.java)

        // Exibe a mensagem de erro ao usuario
        assertThat((state as RideHistoryState.Error).message).isEqualTo("Nenhuma corrida encontrada para o usuário e motorista selecionado.")
    }

    // Lê o arquivos Json do diretório resources
    private fun getJsonContent(fileName: String): String {
        val uri = this.javaClass.classLoader?.getResource(fileName)
        val file = File(uri?.path ?: throw IllegalArgumentException("File not found: $fileName"))
        return file.readText()
    }
}