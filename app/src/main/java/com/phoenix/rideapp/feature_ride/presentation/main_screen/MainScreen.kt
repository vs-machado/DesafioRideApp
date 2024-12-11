package com.phoenix.rideapp.feature_ride.presentation.main_screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * A main screen é responsável por fornecer ao usuário a interface de solicitação de viagens.
 * Nela, o usuário preenche o id de usuário, endereço de origem e destino. Ao clicar no botão "Solicitar viagem",
 * o usuário é redirecionado para a TravelRequestScreen.
 */
@Composable
fun MainScreen(
    viewModel: RideEstimateSharedViewModel,
    onNavigateToRidePricingScreen: () -> Unit
) {

    var userId by rememberSaveable { mutableStateOf("") }
    var originAddress by rememberSaveable { mutableStateOf("") }
    var destinationAddress by rememberSaveable { mutableStateOf("") }
    val priceCalculationState by viewModel.priceCalculationState.collectAsStateWithLifecycle()

    // Quando o fetching dos preços da viagem é bem sucedido e há opções de viagens disponíveis ao usuário,
    // salva as opções de viagem no sharedViewModel e reseta o estado do priceCalculationState.
    LaunchedEffect(priceCalculationState) {
        if (priceCalculationState is PriceCalculationState.Success) {
            val estimateResult = (priceCalculationState as PriceCalculationState.Success).rideEstimate

            estimateResult.fold(
                onSuccess = { estimate ->
                    val rideOptions = estimate.options

                    if(rideOptions.isNotEmpty()){
                        viewModel.saveRideEstimate(estimate)
                        viewModel.resetPriceCalculationState()
                    }
                },
                onFailure = { error ->
                    Log.d("debug", error.message.toString())
                }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Text(
            text = "Olá, passageiro!",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "Para solicitar uma viagem, insira seu ID de usuário:",
            modifier = Modifier.padding(16.dp)
        )
        OutlinedTextField(
            value = userId,
            onValueChange = { userId = it },
            label = { Text("ID de usuário") },
            leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = "ID de usuário") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = "Local de partida:",
            modifier = Modifier.padding(16.dp)
        )
        OutlinedTextField(
            value = originAddress,
            onValueChange = { originAddress = it },
            label = { Text("Endereço de origem") },
            leadingIcon = { Icon(Icons.Outlined.Home, contentDescription = "Endereço de origem") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Text(
            text = "Local de destino:",
            modifier = Modifier.padding(16.dp)
        )
        OutlinedTextField(
            value = destinationAddress,
            onValueChange = { destinationAddress = it },
            label = { Text("Endereço do destino") },
            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = "Endereço de origem") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.padding(16.dp))
        Button(
            onClick = { viewModel.fetchRidePrices(
                userId = userId,
                origin = originAddress,
                destination = destinationAddress
            ) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp) // Corresponde a altura dos textfields
                .padding(horizontal = 16.dp)
        ) {
            Text("Solicitar viagem")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Quando o app estiver calculando os preços da viagem, informa o usuário e exibe
        // um CircularProgressIndicator. Ao concluir o fetching, caso os campos não estejam vazios e
        // haja opções de viagem disponíveis, navega para a próxima tela.
        when(priceCalculationState) {
            is PriceCalculationState.Idle -> {}
            is PriceCalculationState.Loading -> {
                Text(
                    text = "Calculando o preço da viagem...",
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            // Exibe uma mensagem de erro caso o fetching das opções de viagem falhe.
            is PriceCalculationState.Error -> {
                Text(
                    text = (priceCalculationState as PriceCalculationState.Error).message,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            is PriceCalculationState.Success -> {
                val estimateResult = (priceCalculationState as PriceCalculationState.Success).rideEstimate

                estimateResult.fold(
                    // Navega para RidePricesScreen quando o fetching é bem sucedido e há opções de viagem disponíveis.
                    // A lista de opções de viagem disponíveis é salva no bloco do LaunchedEffect.
                    onSuccess = { estimate ->
                        val rideOptions = estimate.options

                        if(rideOptions.isNotEmpty()) {
                            onNavigateToRidePricingScreen()
                        } else {
                            Text(
                                text = "Não há opções de viagem disponíveis para o endereço selecionado. Caso necessário, corrija o endereço e tente novamente.",
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        }
                    },
                    onFailure = { error ->
                        Log.d("debug", error.message.toString())
                        Text(
                            text = error.message.toString(),
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }
                )
            }
        }
    }


}

@Preview
@Composable
fun MainScreenPreview() {
//    MainScreen()
}