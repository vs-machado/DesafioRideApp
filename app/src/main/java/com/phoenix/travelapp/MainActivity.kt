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
import com.phoenix.travelapp.feature_travel.presentation.main_screen.MainScreen
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
                                onNavigateToTravelPricesScreen = {
                                    navController.navigate(TravelPrices)
                                }
                            )
                        }
                        // Os preços da viagem são exibidos ao usuário
                        composable<TravelPrices> {
                            //TravelPricesScreen()
                        }
                    }
                }
            }
        }
    }

    @Serializable
    object Home

    @Serializable
    object TravelPrices
}

