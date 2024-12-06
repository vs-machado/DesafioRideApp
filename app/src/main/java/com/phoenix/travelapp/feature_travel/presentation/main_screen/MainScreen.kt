package com.phoenix.travelapp.feature_travel.presentation.main_screen

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * A main screen é responsável por fornecer ao usuário a interface de solicitação de viagens.
 * Nela, o usuário preenche o id de usuário, endereço de origem e destino. Ao clicar no botão "Solicitar viagem",
 * o usuário é redirecionado para a TravelRequestScreen.
 */
@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = viewModel() //Substituir por hiltViewModel()
) {

    var userId by remember { mutableStateOf("") }
    var originAddress by remember { mutableStateOf("") }
    var destinationAddress by remember { mutableStateOf("") }

    val priceCalculationState by viewModel.priceCalculationState.collectAsState()

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
            onClick = {
                // Chamar o fetching dos preços usando viewmodel
         },
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
                //onNavigateToTravelOptionsScreen()
            }
        }
    }


}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}