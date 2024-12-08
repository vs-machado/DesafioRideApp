package com.phoenix.rideapp.feature_ride.domain.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

/**
 *  Representa a resposta da API de estimativa de viagem (RideApiService.kt)
 *
 *  @property destination Coordenadas de destino da viagem
 *  @property distance Distância total da viagem
 *  @property duration Duração total da viagem
 *  @property options Motoristas disponíveis para a viagem e seus respectivos dados
 *  @property origin Coordenadas de origem da viagem
 *  @property routeResponse Informações detalhadas da rota
 */
data class RideEstimate(
    val destination: Destination,
    val distance: Int,
    val duration: Int,
    val options: List<Option>,
    val origin: Origin,
    val routeResponse: RouteResponse
)

data class Destination(
    val latitude: Double,
    val longitude: Double
)

data class Origin(
    val latitude: Double,
    val longitude: Double
)

/**
 * Representa uma opção de transporte disponível
 *
 * @property description Descrição fornecida pelo motorista
 * @property id Identificador único do motorista
 * @property name Nome do motorista
 * @property review Avaliações e comentários dos usuários sobre o motorista
 * @property value Preço estimado da viagem
 * @property vehicle Modelo do veículo utilizado pelo motorista
 */
@Serializable
data class Option(
    val description: String,
    val id: Int,
    val name: String,
    val review: Review,
    val value: Double,
    val vehicle: String
)

data class RouteResponse(
    val geocodingResults: GeocodingResults,
    val routes: List<Route>
)

/**
 * Representa a avaliação dos motoristas
 *
 * @property comment Comentários dos usuários
 * @property rating Avaliação do motorista
 */
@Serializable
data class Review(
    val comment: String,
    val rating: Int
)

/**
 * Apresenta o tipo e o identificador de cada ponto do endereço
 * @property destination Destino
 * @property origin Origem
 */
data class GeocodingResults(
    val destination: DestinationX,
    val origin: OriginX
)

/**
 * Representa a rota de viagem, com instruções detalhadas de navegação.
 */
data class Route(
    val description: String,
    val distanceMeters: Int,
    val duration: String,
    val legs: List<Leg>,
    val localizedValues: LocalizedValuesXX,
    val polyline: PolylineXX,
    val polylineDetails: PolylineDetails,
    val routeLabels: List<String>,
    val staticDuration: String,
    val travelAdvisory: TravelAdvisory,
    val viewport: Viewport,
    val warnings: List<String>
)

/**
 * Fornece o status do geocodificador, o identificador do local e o tipo de local
 * @property geocoderStatus Status do geocodificador
 * @property placeId Identificador do local
 * @property type Tipo de local
 */
data class DestinationX(
    val geocoderStatus: GeocoderStatus,
    val placeId: String,
    val type: List<String>
)

data class OriginX(
    val geocoderStatus: GeocoderStatus,
    val placeId: String,
    val type: List<String>
)

class GeocoderStatus

/**
 * Representa a rota de viagem
 * @property distanceMeters Distância em metros entre o startLocation e o endLocation
 * @property staticDuration Tempo estimado necessário para percorrer uma determinada distância. Útil para fornecer uma estimativa de tempo de chegada.
 * @property polyline Representação compactada da rota em formato de linha poligonal
 * @property startLocation Coordenadas de início da instrução
 * @property endLocation Coordenadas de fim da instrução
 * @property steps Lista de passos para percorrer a instrução
 */
data class Leg(
    val distanceMeters: Int,
    val duration: String,
    val endLocation: EndLocation,
    val localizedValues: LocalizedValuesXX,
    val polyline: PolylineXX,
    val startLocation: StartLocation,
    val staticDuration: String,
    val steps: List<Step>
)

/**
 * Apresenta a distância entre os pontos em quilômetros e o tempo estimado em minutos
 * @property distance Distância entre os pontos
 * @property duration Tempo estimado
 * @property staticDuration Tempo estático

 */
data class LocalizedValuesXX(
    val distance: Distance,
    val duration: Duration,
    val staticDuration: StaticDuration
)

data class Distance(
    val text: String
)

/**
 * Representa a rota de viagem completa em polilinhas
 */
data class PolylineXX(
    val encodedPolyline: String
)

class PolylineDetails

class TravelAdvisory

data class Viewport(
    val high: High,
    val low: Low
)

// Coordenadas do ponto final
data class EndLocation(
    val latLng: LatLng
)

// Coordenadas do ponto inicial
data class StartLocation(
    val latLng: LatLng
)

/**
 * Representa uma instrução da rota de viagem
 * @property distanceMeters Distância em metros entre o startLocation e o endLocation
 * @property staticDuration Tempo estimado necessário para percorrer uma determinada distância. Útil para fornecer uma estimativa de tempo de chegada.
 * @property polyline Representação compactada da rota em formato de linha poligonal
 * @property startLocation Coordenadas de início da instrução
 * @property endLocation Coordenadas de fim da instrução
 * @property navigationInstruction Instrução de navegação
 * @property localizedValues Indica a distância entre os pontos em quilômetros e o tempo estimado em minutos
 * @property travelMode Modo de transporte utilizado para percorrer a instrução
 */
data class Step(
    val distanceMeters: Int,
    val endLocation: EndLocation,
    val localizedValues: LocalizedValuesX,
    val navigationInstruction: NavigationInstruction,
    val polyline: PolylineXX,
    val startLocation: StartLocation,
    val staticDuration: String,
    val travelMode: String
)

data class StaticDuration(
    val text: String
)

data class LocalizedValuesX(
    val distance: Distance,
    val staticDuration: StaticDuration
)

// Representa as coordenadas de latitude e longitude de cada ponto da rota.
data class LatLng(
    val latitude: Double,
    val longitude: Double
)

data class Duration(
    val text: String
)

/**
 * Representa uma instrução de navegação
 * @property maneuver Manobra de navegação
 * @property instructions Instruções detalhadas de navegação
 */
data class NavigationInstruction(
    val instructions: String,
    val maneuver: String
)

data class High(
    val latitude: Double,
    val longitude: Double
)

data class Low(
    val latitude: Double,
    val longitude: Double
)

/**
 * Representa uma resposta de erro da API de estimativa de viagem
 * @property errorCode Código de erro
 * @property errorDescription Descrição do erro
 */
data class ErrorResponse(
    @SerializedName("error_code")
    val errorCode: String,
    @SerializedName("error_description")
    val errorDescription: String
)