package com.phoenix.travelapp.di

import com.phoenix.travelapp.feature_ride.data.api.RIDE_API_BASE_URL
import com.phoenix.travelapp.feature_ride.data.api.RideApiService
import com.phoenix.travelapp.feature_ride.data.repository.RideApiRepositoryImpl
import com.phoenix.travelapp.feature_ride.domain.model.ride_api.repository.RideApiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(RIDE_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
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
        rideApiService: RideApiService
    ): RideApiRepository {
        return RideApiRepositoryImpl(rideApiService)
    }
}