package com.yash091099.ChiragFarmersApp.di

import android.content.Context
import com.yash091099.ChiragFarmersApp.data.local.ChiragDataStore
import com.yash091099.ChiragFarmersApp.data.local.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideAuthDataStore(
        @ApplicationContext context: Context
    ): ChiragDataStore {
        return ChiragDataStore(context)
    }

    @Provides
    @Singleton
    fun provideTokenManager(
        chiragDataStore: ChiragDataStore
    ): TokenManager {
        return TokenManager(chiragDataStore)
    }
}