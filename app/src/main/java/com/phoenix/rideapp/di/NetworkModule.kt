package com.phoenix.rideapp.di

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import com.phoenix.rideapp.core.helpers.LocationHelper
import com.phoenix.rideapp.feature_ride.data.api.RIDE_API_BASE_URL
import com.phoenix.rideapp.feature_ride.data.api.RideApiService
import com.phoenix.rideapp.feature_ride.data.repository.RideApiRepositoryImpl
import com.phoenix.rideapp.feature_ride.domain.model.ride_api.RideApiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(RIDE_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(
                GsonBuilder()
                    .setStrictness(Strictness.LENIENT)
                    .create()
            ))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideRideApiService(retrofit: Retrofit): RideApiService {
        return retrofit.create(RideApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRideRepository(
        rideApiService: RideApiService,
        locationHelper: LocationHelper
    ): RideApiRepository {
        return RideApiRepositoryImpl(rideApiService, locationHelper)
    }
}