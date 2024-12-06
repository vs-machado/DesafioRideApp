package com.phoenix.travelapp.feature_ride.presentation.main_screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.phoenix.travelapp.feature_ride.domain.model.RideEstimateValueResponse

/**
 * A main screen é responsável por fornecer ao usuário a interface de solicitação de viagens.
 * Nela, o usuário preenche o id de usuário, endereço de origem e destino. Ao clicar no botão "Solicitar viagem",
 * o usuário é redirecionado para a TravelRequestScreen.
 */
@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = viewModel(), //Substituir por hiltViewModel()
    onNavigateToRidePricingScreen: (List<RideEstimateValueResponse.Option>) -> Unit
) {

    var userId by rememberSaveable { mutableStateOf("") }
    var originAddress by rememberSaveable { mutableStateOf("") }
    var destinationAddress by rememberSaveable { mutableStateOf("") }
    val priceCalculationState by viewModel.priceCalculationState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize()
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
            value = "",
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
            value = "",
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
            value = "",
            onValueChange = { destinationAddress = it },
            label = { Text("Endereço do destino") },
            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = "Endereço de origem") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.padding(16.dp))
        FilledTonalButton(
            onClick = { viewModel.fetchRidePrices(
                customerId = userId,
                originAddress = originAddress,
                destinationAddress = destinationAddress
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
        // um CircularProgressIndicator. Ao concluir o fetching, navega para a próxima tela.
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
            is PriceCalculationState.Error -> {
                Text(
                    text = "Erro ao calcular o preço da viagem.",
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            is PriceCalculationState.Success -> {
                val options = (priceCalculationState as PriceCalculationState.Success).rideOptions

                options.fold(
                    onSuccess = { rideOptions ->
                        onNavigateToRidePricingScreen(rideOptions)
                    },
                    onFailure = { error ->
                        Log.d("debug", error.message.toString())
                        Text(
                            text = "Não há opções de viagem disponíveis.",
                            modifier = Modifier.padding(horizontal = 16.dp)
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