package com.phoenix.rideapp.feature_ride.presentation.ride_prices_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.phoenix.rideapp.BuildConfig
import com.phoenix.rideapp.feature_ride.domain.model.LatLng
import com.phoenix.rideapp.feature_ride.presentation.main_screen.RideEstimateSharedViewModel
import java.net.URLEncoder

@Composable
fun RidePricesScreen(
    viewModel: RideEstimateSharedViewModel
) {
    val route = viewModel.rideEstimate.routeResponse.routes[0]

    // Rota em polilinha codificada. Utilizada para desenhar o mapa da viagem.
    val ridePolyline = route.legs[0].polyline.encodedPolyline

    // Coordenadas de origem e destino da viagem. Utilizadas para marcar os pontos de início e fim da viagem.
    val startLatLng = route.legs[0].startLocation.latLng
    val endLatLng = route.legs[0].endLocation.latLng

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Opções de corrida",
            modifier = Modifier
                .align(Alignment.Start)
                .padding(16.dp)
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = "Percurso da viagem",
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp, end = 16.dp)
        )
        StaticMap(
            mapRes = "640x360",
            encodedPolyline = ridePolyline,
            startLatLng = startLatLng,
            endLatLng = endLatLng,
            apiKey = BuildConfig.gMapsApiKey
        )
    }
}

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
                .padding(16.dp)
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