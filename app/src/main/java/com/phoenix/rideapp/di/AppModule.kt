package com.phoenix.rideapp.di

import android.content.Context
import com.phoenix.rideapp.core.helpers.LocationHelper
import com.phoenix.rideapp.core.helpers.LocationHelperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideLocationHelper(
        @ApplicationContext context: Context
    ): LocationHelper {
        return LocationHelperImpl(context)
    }
}