package com.yash091099.ChiragFarmersApp.di

import com.yash091099.ChiragFarmersApp.data.local.ChiragDataStore
import com.yash091099.ChiragFarmersApp.data.remote.AuthApiService
import com.yash091099.ChiragFarmersApp.data.remote.CropAnalysisApiService
import com.yash091099.ChiragFarmersApp.data.remote.CartApiService
import com.yash091099.ChiragFarmersApp.data.remote.NotificationApiService
import com.yash091099.ChiragFarmersApp.data.remote.OrderApiService
import com.yash091099.ChiragFarmersApp.data.remote.ProductApiService
import com.yash091099.ChiragFarmersApp.data.repository.AuthRepository
import com.yash091099.ChiragFarmersApp.data.repository.CropAnalysisRepositoryImpl
import com.yash091099.ChiragFarmersApp.data.repository.CartRepositoryImpl
import com.yash091099.ChiragFarmersApp.data.repository.NotificationRepositoryImpl
import com.yash091099.ChiragFarmersApp.data.repository.OrderRepositoryImpl
import com.yash091099.ChiragFarmersApp.data.repository.ProductRepositoryImpl
import com.yash091099.ChiragFarmersApp.domain.repository.CropAnalysisRepository
import com.yash091099.ChiragFarmersApp.domain.repository.CartRepository
import com.yash091099.ChiragFarmersApp.domain.repository.OrderRepository
import com.yash091099.ChiragFarmersApp.domain.repository.NotificationRepository
import com.yash091099.ChiragFarmersApp.domain.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import android.content.Context
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        api: AuthApiService, dataStore: ChiragDataStore
    ): AuthRepository {
        return AuthRepository(api, dataStore)
    }

    @Provides
    @Singleton
    fun provideProductRepository(
        api: ProductApiService, chiragDataStore: ChiragDataStore
    ): ProductRepository {
        return ProductRepositoryImpl(api, chiragDataStore)
    }

    @Provides
    @Singleton
    fun provideCartRepository(
        api: CartApiService, chiragDataStore: ChiragDataStore
    ): CartRepository {
        return CartRepositoryImpl(api, chiragDataStore)
    }

    @Provides
    @Singleton
    @Suppress("unused")
    fun provideCropAnalysisRepository(
        api: CropAnalysisApiService,
        chiragDataStore: ChiragDataStore,
        @ApplicationContext context: Context
    ): CropAnalysisRepository {
        return CropAnalysisRepositoryImpl(api, chiragDataStore, context)
    }

    @Provides
    @Singleton
    @Suppress("unused")
    fun provideNotificationRepository(
        api: NotificationApiService, chiragDataStore: ChiragDataStore
    ): NotificationRepository {
        return NotificationRepositoryImpl(api, chiragDataStore)
    }

    @Provides
    @Singleton
    fun provideOrderRepository(
        api: OrderApiService, chiragDataStore: ChiragDataStore
    ): OrderRepository {
        return OrderRepositoryImpl(api, chiragDataStore)
    }
}