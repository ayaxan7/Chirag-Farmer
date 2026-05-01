package com.yash091099.ChiragFarmersApp.di

import com.yash091099.ChiragFarmersApp.data.local.ChiragDataStore
import com.yash091099.ChiragFarmersApp.data.remote.AuthApiService
import com.yash091099.ChiragFarmersApp.data.remote.CartApiService
import com.yash091099.ChiragFarmersApp.data.remote.OrderApiService
import com.yash091099.ChiragFarmersApp.data.remote.ProductApiService
import com.yash091099.ChiragFarmersApp.data.repository.AuthRepository
import com.yash091099.ChiragFarmersApp.data.repository.CartRepositoryImpl
import com.yash091099.ChiragFarmersApp.data.repository.OrderRepositoryImpl
import com.yash091099.ChiragFarmersApp.data.repository.ProductRepositoryImpl
import com.yash091099.ChiragFarmersApp.domain.repository.CartRepository
import com.yash091099.ChiragFarmersApp.domain.repository.OrderRepository
import com.yash091099.ChiragFarmersApp.domain.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
    fun provideOrderRepository(
        api: OrderApiService, chiragDataStore: ChiragDataStore
    ): OrderRepository {
        return OrderRepositoryImpl(api, chiragDataStore)
    }
}