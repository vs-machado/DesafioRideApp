package com.phoenix.rideapp.feature_ride.presentation.ride_prices_screen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.phoenix.rideapp.BuildConfig
import com.phoenix.rideapp.feature_ride.domain.model.ride_api.LatLng
import com.phoenix.rideapp.feature_ride.presentation.main_screen.RideConfirmationState
import com.phoenix.rideapp.feature_ride.presentation.main_screen.RideEstimateSharedViewModel
import kotlinx.coroutines.delay
import java.net.URLEncoder

/**
 * Tela de exibição das opções de corrida.
 * Exibe o percurso da viagem e as opções de motoristas disponíveis.
 * Ao selecionar um motorista, um floating action button é exibido para confirmar a corrida.
 */
@Composable
fun RidePricesScreen(
    viewModel: RideEstimateSharedViewModel,
    onNavigateToRaceHistoryScreen: (Int) -> Unit
) {
    val context = LocalContext.current
    val confirmationState by viewModel.rideConfirmationState.collectAsStateWithLifecycle()
    var isShowingToast by remember { mutableStateOf(false) }

    val rideDetails = viewModel.rideEstimate
    val route = viewModel.rideEstimate.routeResponse.routes[0]

    // Rota em polilinha codificada. Utilizada para desenhar o mapa da viagem.
    val ridePolyline = route.legs[0].polyline.encodedPolyline

    // Coordenadas de origem e destino da viagem. Utilizadas para marcar os pontos de início e fim da viagem.
    val startLatLng = route.legs[0].startLocation.latLng
    val endLatLng = route.legs[0].endLocation.latLng
    
    // Caso a confirmação da viagem seja bem sucedida navega para a próxima tela.
    // Ao clicar no botão de confirmar viagem a variável isShowingToast previne que o Toast
    // seja chamado novamente caso o usuário clique várias vezes no botão.
    LaunchedEffect(confirmationState) {
        when(confirmationState) {
            is RideConfirmationState.Success -> {
                viewModel.resetRideConfirmationState()
                onNavigateToRaceHistoryScreen(viewModel.driverId)

                if (!isShowingToast) {
                    isShowingToast = true
                    Toast.makeText(
                        context,
                        "Viagem confirmada com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                    delay(2000)
                    isShowingToast = false
                }
            }
            is RideConfirmationState.Error -> {
                if (!isShowingToast) {
                    isShowingToast = true
                    Toast.makeText(
                        context,
                        (confirmationState as RideConfirmationState.Error).message,
                        Toast.LENGTH_SHORT
                    ).show()
                    delay(2000)
                    isShowingToast = false
                    viewModel.resetRideConfirmationState()
                }
            }
            is RideConfirmationState.Idle -> {}
            is RideConfirmationState.Loading -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(bottom = 16.dp)
    ) {
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = "Percurso da viagem",
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 18.sp
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(start = 16.dp, end = 16.dp)
        )
        Spacer(modifier = Modifier.padding(4.dp))
        StaticMap(
            mapRes = "640x360",
            encodedPolyline = ridePolyline,
            startLatLng = startLatLng,
            endLatLng = endLatLng,
            apiKey = BuildConfig.gMapsApiKey
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = "Escolha o motorista: ",
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 18.sp
            ),
            modifier = Modifier.padding(start = 24.dp, end = 16.dp)
        )

        // Exibe a lista de motoristas disponíveis. Quando o usuário clica no botão
        // "Confirmar" a função confirmRide é chamada.
        DriverList(
            rideDetails = rideDetails,
            onRideConfirmation = { option ->
                viewModel.confirmRide(
                    userId = viewModel.customerId,
                    destination = viewModel.destinationAddress,
                    distance = rideDetails.distance,
                    driverId = option.id,
                    driverName = option.name,
                    duration = rideDetails.duration,
                    origin = viewModel.originAddress,
                    value = option.value
                )
            }
        )

    }
}

/**
 *  Exibe a rota a ser percorrida.
 *  @property mapRes Resolução do mapa.
 *  @property encodedPolyline Polilinha codificada da rota.
 *  @property startLatLng Coordenadas de início da viagem.
 *  @property endLatLng Coordenadas de fim da viagem.
 *  @property apiKey API key da Static Maps API.
 */
@Composable
fun StaticMap(
    mapRes: String,
    encodedPolyline: String,
    startLatLng: LatLng,
    endLatLng: LatLng,
    apiKey: String
) {

    val mapUrl = "https://maps.googleapis.com/maps/api/staticmap?" +
            "size=$mapRes&maptype=roadmap&path=color:0x0000ffff|weight:5|enc:${URLEncoder.encode(encodedPolyline, "UTF-8")}" +
            "&markers=color:green|label:I|${startLatLng.latitude},${startLatLng.longitude}" +
            "&markers=color:red|label:F|${endLatLng.latitude},${endLatLng.longitude}" +
            "&format=jpg&key=$apiKey"

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = mapUrl,
            contentDescription = "Mapa do percurso",
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
        )
    }
}

@Preview
@Composable
fun RidePricesScreenPreview(){
//    RidePricesScreen()
}