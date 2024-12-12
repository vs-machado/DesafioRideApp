package com.phoenix.rideapp.core.helpers

import android.content.Context
import com.phoenix.rideapp.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Classe que fornece as strings para os viewmodels e implementações de classe
 *
 * @property context Contexto da aplicação
 */
class LocationHelperImpl @Inject constructor(
    @ApplicationContext private val context: Context
): LocationHelper {
    override fun getErrorEmptyOrigin() = context.getString(R.string.error_empty_origin)
    override fun getErrorEmptyDestination() = context.getString(R.string.error_empty_destination)
    override fun getErrorEmptyUserId() = context.getString(R.string.error_empty_user_id)
    override fun getErrorSameAddresses() = context.getString(R.string.error_same_addresses)
    override fun getErrorUnknown() = context.getString(R.string.error_unknown)
    override fun getErrorRideConfirmationFailed() = context.getString(R.string.error_ride_confirmation_failed)
    override fun getErrorRideRequestFailed(): String = context.getString(R.string.error_ride_request_failed)
    override fun getErrorNoInternet() = context.getString(R.string.error_no_internet)
    override fun getErrorInvalidData() = context.getString(R.string.error_invalid_data)
    override fun getErrorServer() = context.getString(R.string.error_server)
    override fun getErrorContactSupport() = context.getString(R.string.error_contact_support)
    override fun getErrorInvalidProvidedData() = context.getString(R.string.error_invalid_provided_data)
    override fun getErrorDriverSelectionFailed() = context.getString(R.string.error_driver_selection_failed)
    override fun getErrorInvalidMileage() = context.getString(R.string.error_invalid_mileage)
    override fun getErrorInvalidDriver() = context.getString(R.string.error_invalid_driver)
    override fun getErrorNoRidesFound() = context.getString(R.string.error_no_rides_found)
    override fun getErrorUnexpected() = context.getString(R.string.error_unexpected)
    override fun getErrorFetchHistory() = context.getString(R.string.error_fetch_history)
}