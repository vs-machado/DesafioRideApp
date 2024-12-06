package com.phoenix.travelapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.phoenix.travelapp.feature_ride.domain.model.RideEstimateValueResponse
import com.phoenix.travelapp.feature_ride.presentation.main_screen.MainScreen
import com.phoenix.travelapp.feature_ride.presentation.ride_prices_screen.RidePricesScreen
import com.phoenix.travelapp.ui.theme.TravelAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TravelAppTheme(dynamicColor = false) {
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Home,
                        modifier = Modifier.background(MaterialTheme.colorScheme.background)
                    ) {
                        // Tela inicial. Usuário insere o ID de usuário, endereço de origem e destino e solicita o cálculo dos preços da viagem
                        composable<Home> {
                            MainScreen(
                                onNavigateToRidePricingScreen = { optionsList ->
                                    navController.navigate(RidePrices(optionsList))
                                }
                            )
                        }
                        // Tela onde o preço das viagens são exibidos ao usuário
                        composable<RidePrices> { backStackEntry ->
                            val ridePrices: RidePrices = backStackEntry.toRoute()
                            RidePricesScreen(ridePrices.optionsList)
                        }
                    }
                }
            }
        }
    }

    @Serializable
    object Home

    @Serializable
    data class RidePrices(val optionsList: List<RideEstimateValueResponse.Option>)
}

