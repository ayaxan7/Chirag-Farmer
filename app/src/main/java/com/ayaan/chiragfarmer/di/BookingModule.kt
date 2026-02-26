package com.ayaan.chiragfarmer.di

import com.ayaan.chiragfarmer.data.remote.BookingApiService
import com.ayaan.chiragfarmer.data.repository.BookingRepositoryImpl
import com.ayaan.chiragfarmer.domain.repository.BookingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BookingModule {

    @Provides
    @Singleton
    fun provideBookingApiService(retrofit: Retrofit): BookingApiService {
        return retrofit.create(BookingApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideBookingRepository(
        apiService: BookingApiService,
        authDataStore: com.ayaan.chiragfarmer.data.local.AuthDataStore
    ): BookingRepository {
        return BookingRepositoryImpl(apiService, authDataStore)
    }
}
