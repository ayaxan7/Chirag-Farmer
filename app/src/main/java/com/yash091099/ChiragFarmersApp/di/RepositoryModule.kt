package com.yash091099.ChiragFarmersApp.di

import com.yash091099.ChiragFarmersApp.data.local.AuthDataStore
import com.yash091099.ChiragFarmersApp.data.remote.AuthApiService
import com.yash091099.ChiragFarmersApp.data.remote.CartApiService
import com.yash091099.ChiragFarmersApp.data.remote.ProductApiService
import com.yash091099.ChiragFarmersApp.data.repository.AuthRepository
import com.yash091099.ChiragFarmersApp.data.repository.CartRepositoryImpl
import com.yash091099.ChiragFarmersApp.data.repository.ProductRepositoryImpl
import com.yash091099.ChiragFarmersApp.domain.repository.CartRepository
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
        api: AuthApiService, dataStore: AuthDataStore
    ): AuthRepository {
        return AuthRepository(api, dataStore)
    }

    @Provides
    @Singleton
    fun provideProductRepository(
        api: ProductApiService, authDataStore: AuthDataStore
    ): ProductRepository {
        return ProductRepositoryImpl(api, authDataStore)
    }

    @Provides
    @Singleton
    fun provideCartRepository(
        api: CartApiService, authDataStore: AuthDataStore
    ): CartRepository {
        return CartRepositoryImpl(api, authDataStore)
    }
}