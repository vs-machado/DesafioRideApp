package com.phoenix.rideapp.feature_ride.presentation.main_screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.phoenix.rideapp.R
import com.phoenix.rideapp.feature_ride.domain.util.debounceHandler
import com.phoenix.rideapp.ui.theme.WhiteSnow
import com.phoenix.rideapp.feature_ride.presentation.ride_prices_screen.RidePricesScreen

/**
 * A main screen é responsável por fornecer ao usuário a interface de solicitação de viagens.
 * Nela, o usuário preenche o id de usuário, endereço de origem e destino. Ao clicar no botão "Solicitar viagem",
 * o usuário é redirecionado para a RidePricesScreen.
 *
 * @param viewModel SharedViewModel contendo a lógica do cálculo e confirmação da viagem
 * @param onNavigateToRidePricingScreen Callback para navegar para a [RidePricesScreen]
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
        modifier = Modifier.systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.rideapp_logo),
            contentDescription = stringResource(R.string.app_logo_description),
            modifier = Modifier.height(142.dp)
        )
        Text(
            text = stringResource(R.string.greeting_title),
            modifier = Modifier.padding(horizontal = 32.dp),
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = stringResource(R.string.greeting_subtitle),
            modifier = Modifier.padding(horizontal = 32.dp),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = WhiteSnow
            ),
            shape = RoundedCornerShape(32.dp)
        ) {
            Column {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.ride_data),
                    modifier = Modifier.padding(horizontal = 24.dp),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                val isUserIdFocused = remember { mutableStateOf(false) }
                OutlinedTextField(
                    value = userId,
                    onValueChange = { userId = it },
                    label = { Text(stringResource(R.string.user_id)) },
                    leadingIcon = { if(isUserIdFocused.value) {
                        Icon(Icons.Filled.Person, contentDescription = stringResource(R.string.user_id))
                    } else {
                        Icon(Icons.Outlined.Person, contentDescription = stringResource(R.string.user_id))
                    }},
                    shape = RoundedCornerShape(32.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .onFocusChanged { focusState ->
                            isUserIdFocused.value = focusState.isFocused
                        }
                )
                Spacer(modifier = Modifier.height(8.dp))

                val isOriginAddressFocused = remember { mutableStateOf(false) }
                OutlinedTextField(
                    value = originAddress,
                    onValueChange = { originAddress = it },
                    label = { Text(stringResource(R.string.origin_address)) },
                    leadingIcon = { if(isOriginAddressFocused.value) {
                        Icon(Icons.Filled.Home, contentDescription = stringResource(R.string.origin_address))
                    } else {
                        Icon(Icons.Outlined.Home, contentDescription = stringResource(R.string.origin_address))
                    }},
                    shape = RoundedCornerShape(32.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .onFocusChanged { focusState ->
                            isOriginAddressFocused.value = focusState.isFocused
                        }
                )
                Spacer(modifier = Modifier.height(8.dp))

                val isDestinationAddressFocused = remember { mutableStateOf(false) }
                OutlinedTextField(
                    value = destinationAddress,
                    onValueChange = { destinationAddress = it },
                    label = { Text(stringResource(R.string.destination_address)) },
                    leadingIcon = { if(isDestinationAddressFocused.value) {
                        Icon(Icons.Filled.LocationOn, contentDescription = stringResource(R.string.destination_address))
                    } else {
                        Icon(Icons.Outlined.LocationOn, contentDescription = stringResource(R.string.destination_address))
                    }},
                    shape = RoundedCornerShape(32.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .onFocusChanged { focusState ->
                            isDestinationAddressFocused.value = focusState.isFocused
                        }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = debounceHandler {
                        viewModel.fetchRidePrices(
                            userId = userId,
                            origin = originAddress,
                            destination = destinationAddress
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp) // Corresponde a altura dos textfields
                        .padding(horizontal = 16.dp)

                ) {
                    Text(stringResource(R.string.request_ride))
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

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
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Red,
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
                                text = "Não há motoristas disponíveis para esse endereço." +
                                        " Corrija o endereço e tente novamente.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Red,
                                modifier = Modifier
                                    .padding(horizontal = 24.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                    },
                    onFailure = { error ->
                        Text(
                            text = error.message.toString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Red,
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