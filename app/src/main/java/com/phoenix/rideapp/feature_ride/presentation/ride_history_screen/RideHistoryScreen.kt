package com.phoenix.rideapp.feature_ride.presentation.ride_history_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.phoenix.rideapp.feature_ride.domain.util.debounceHandler
import com.phoenix.rideapp.ui.theme.LightGreen
import com.phoenix.rideapp.feature_ride.presentation.ride_prices_screen.RidePricesScreen

/**
 * Tela que exibe o histórico de corridas correspondentes para um dado usuário.
 * O histórico de corridas é exibido quando um id válido é inserido pelo usuário.
 *
 * As corridas são exibidas em ordem cronológica (mais recentes) e podem ser filtradas ao
 * selecionar um motorista em específico.
 *
 * @param driverId Id do motorista selecionado na [RidePricesScreen]
 * @param viewModel ViewModel contendo a lógica do fetching do histórico de corridas
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RideHistoryScreen(
    driverId: Int,
    viewModel: RideHistoryViewModel = hiltViewModel()
) {
    val rideHistoryState by viewModel.rideHistoryState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ){
        // Id a ser consultado. Preenchido pelo usuário.
        var userId by rememberSaveable { mutableStateOf("") }

        // Armazena o motorista selecionado quando o usuário clica no botão "Consultar corridas realizadas"
        var selectedDriverName by remember { mutableStateOf("") }

        // Armazena o motorista selecionado no DropdownMenu
        var tempDriverName by remember { mutableStateOf("Todos os motoristas") }
        var expanded by remember { mutableStateOf(false) }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Histórico de corridas",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = userId,
            onValueChange = { userId = it },
            label = { Text("ID de usuário") },
            leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = "ID de usuário") },
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = tempDriverName,
                onValueChange = {},
                label = { Text("Selecione um motorista") },
                readOnly = true,
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                shape = RoundedCornerShape(32.dp),
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = LightGreen
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                // Opções do filtro de pesquisas
                val driverOptions = listOf("Todos os motoristas", "Homer Simpson", "Dominic Toretto", "James Bond")

                driverOptions.forEach { driver ->
                    DropdownMenuItem(
                        text = { Text(driver) },
                        onClick = {
                            tempDriverName = driver
                            expanded = false
                        }
                    )
                }
            }

        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = debounceHandler {
                if (userId.isNotBlank()) {
                    selectedDriverName = tempDriverName
                    viewModel.fetchRaceHistory(userId, driverId = driverId)
                } else {
                    viewModel.setError("O campo ID de usuário deve ser preenchido.")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp) // Corresponde a altura dos textfields
                .padding(horizontal = 16.dp)
        ) {
            Text("Consultar corridas realizadas")
        }

        when(rideHistoryState) {
            is RideHistoryState.Idle -> {}
            is RideHistoryState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
            is RideHistoryState.Success -> {
                val raceHistory = (rideHistoryState as RideHistoryState.Success).history

                raceHistory.getOrNull()?.let { history ->
                    val sortedRides = when(selectedDriverName) {
                        "Todos os motoristas" -> {
                            history.rides.sortedByDescending { it.date }
                        }

                        else -> {
                            history.rides
                                .filter { it.driver.name == selectedDriverName }
                                .sortedByDescending { it.date }
                        }
                    }

                    if(sortedRides.isNotEmpty()){
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            items(sortedRides) { ride ->
                                RideHistoryItem(ride)
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Nenhuma corrida encontrada para o respectivo motorista.",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(horizontal = 16.dp)
                        )
                    }
                }

            }
            is RideHistoryState.Error -> {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = (rideHistoryState as RideHistoryState.Error).message,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(horizontal = 24.dp)
                )
            }
        }

    }
}

@Preview
@Composable
fun RaceHistoryScreenPreview() {
//    RideHistoryScreen()
}