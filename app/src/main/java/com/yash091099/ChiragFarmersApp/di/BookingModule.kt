package com.yash091099.ChiragFarmersApp.di

import com.yash091099.ChiragFarmersApp.data.remote.BookingApiService
import com.yash091099.ChiragFarmersApp.data.repository.BookingRepositoryImpl
import com.yash091099.ChiragFarmersApp.domain.repository.BookingRepository
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
        chiragDataStore: com.yash091099.ChiragFarmersApp.data.local.ChiragDataStore
    ): BookingRepository {
        return BookingRepositoryImpl(apiService, chiragDataStore)
    }
}
