package com.phoenix.rideapp.feature_ride.presentation.ride_history_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CardColors
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.phoenix.rideapp.R
import com.phoenix.rideapp.feature_ride.domain.model.ride_api.Ride
import com.phoenix.rideapp.feature_ride.domain.util.parseDateTime

/**
 * Layout do card que contém os dados de uma corrida realizada.
 *
 * @param ride Dados da corrida realizada
 */
@Composable
fun RideHistoryItem(
    ride: Ride
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = parseDateTime(ride.date),
           modifier = Modifier.align(Alignment.CenterHorizontally)
        )
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
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Foto de perfil do motorista
                    AsyncImage(
                        model = when(ride.driver.name) {
                            "Homer Simpson" -> "https://www.infomoney.com.br/wp-content/uploads/2019/06/homer-simpson.jpg?fit=900%2C734&quality=50&strip=all"
                            "Dominic Toretto" -> "https://www.shutterstock.com/image-illustration/characters-fast-furious-vin-diesel-600nw-2338870665.jpg"
                            "James Bond" -> "https://pics.craiyon.com/2023-09-28/66d90406deee446f9604d850aa1c7f39.webp"
                            else -> "https://cdn-icons-png.flaticon.com/512/6522/6522516.png"
                        },
                        contentDescription = stringResource(R.string.driver_photo_content_description),
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                    )
                    Column {
                        // Nome do motorista
                        Text(
                            text = ride.driver.name,
                            style = MaterialTheme.typography.titleMedium
                        )
                        // Ponto de origem e destino
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.House,
                                contentDescription = stringResource(R.string.origin_point_content_description),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = ride.origin,
                                style = MaterialTheme.typography.labelMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = stringResource(R.string.destination_point_content_description),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = ride.destination,
                                style = MaterialTheme.typography.labelMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.ride_value, ride.value),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )
                Text(
                    text = stringResource(R.string.ride_distance, ride.distance),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )
                Text(
                    text = stringResource(R.string.ride_duration, ride.duration),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}