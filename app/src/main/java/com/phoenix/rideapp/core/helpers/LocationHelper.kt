package com.phoenix.rideapp.core.helpers

/**
 * Interface que fornece as strings para os viewmodels e implementações de classe
 */
interface LocationHelper {
    fun getErrorEmptyOrigin(): String
    fun getErrorEmptyDestination(): String
    fun getErrorEmptyUserId(): String
    fun getErrorSameAddresses(): String
    fun getErrorUnknown(): String
    fun getErrorRideConfirmationFailed(): String
    fun getErrorRideRequestFailed(): String
    fun getErrorNoInternet(): String
    fun getErrorInvalidData(): String
    fun getErrorServer(): String
    fun getErrorContactSupport(): String
    fun getErrorInvalidProvidedData(): String
    fun getErrorDriverSelectionFailed(): String
    fun getErrorInvalidMileage(): String
    fun getErrorInvalidDriver(): String
    fun getErrorNoRidesFound(): String
    fun getErrorUnexpected(): String
    fun getErrorFetchHistory(): String
}