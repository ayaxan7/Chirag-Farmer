package com.ayaan.chiragfarmer.di

import com.ayaan.chiragfarmer.data.local.AuthDataStore
import com.ayaan.chiragfarmer.data.remote.AuthApiService
import com.ayaan.chiragfarmer.data.remote.ProductApiService
import com.ayaan.chiragfarmer.data.repository.AuthRepository
import com.ayaan.chiragfarmer.data.repository.ProductRepositoryImpl
import com.ayaan.chiragfarmer.domain.repository.ProductRepository
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
}