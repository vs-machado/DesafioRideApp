package com.phoenix.rideapp.feature_ride.presentation.ride_prices_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardColors
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.phoenix.rideapp.R
import com.phoenix.rideapp.feature_ride.domain.model.ride_api.Option
import com.phoenix.rideapp.feature_ride.domain.model.ride_api.RideEstimate
import com.phoenix.rideapp.feature_ride.domain.util.debounceHandler
import com.phoenix.rideapp.feature_ride.presentation.ride_history_screen.RideHistoryScreen

/**
 * LazyColumn que exibe a lista de motoristas disponíveis para a corrida.
 *
 * @param rideDetails Informações dos motoristas disponíveis para a corrida
 * @param onRideConfirmation Navega para a [RideHistoryScreen]
 */
@Composable
fun DriverList(
    rideDetails: RideEstimate,
    onRideConfirmation: (Option) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
    ) {
        items(rideDetails.options) { option ->
            DriverCard(
                option = option,
                onRideConfirmation = onRideConfirmation
            )
        }
    }
}

// Card que exibe as informações do motorista
@Composable
fun DriverCard(
    option: Option,
    onRideConfirmation: (Option) -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardColors(
            contentColor = Color.Black,
            containerColor = Color.White,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.Black
        )
    ) {
        CardContent(
            option = option,
            onRideConfirmation = onRideConfirmation
        )
    }
}

@Composable
private fun CardContent(
    option: Option,
    onRideConfirmation: (Option) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Foto de perfil do motorista
            AsyncImage(
                model = when(option.name) {
                    "Homer Simpson" -> "https://www.infomoney.com.br/wp-content/uploads/2019/06/homer-simpson.jpg?fit=900%2C734&quality=50&strip=all"
                    "Dominic Toretto" -> "https://www.shutterstock.com/image-illustration/characters-fast-furious-vin-diesel-600nw-2338870665.jpg"
                    "James Bond" -> "https://pics.craiyon.com/2023-09-28/66d90406deee446f9604d850aa1c7f39.webp"
                    else -> "https://cdn-icons-png.flaticon.com/512/6522/6522516.png"
                },
                contentDescription = stringResource(R.string.driver_photo),
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
            Column {
                // Nome do motorista
                Text(
                    text = option.name,
                    style = MaterialTheme.typography.titleMedium
                )
                // Veículo do motorista
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = stringResource(R.string.car_icon),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = option.vehicle,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                // Avaliação do motorista. Varia de 1 a 5.
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = stringResource(R.string.star_icon),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = stringResource(R.string.driver_rating, option.review.rating),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Descrição do motorista
        Text(
            text = option.description,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(4.dp))

        // Preço da viagem
        Text(
            text = stringResource(R.string.ride_price, option.value),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(4.dp))

        FilledTonalButton(
            onClick = debounceHandler {
                onRideConfirmation(option)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(
                text = stringResource(R.string.choose_driver),
                color = Color.DarkGray
            )
        }

    }
}
