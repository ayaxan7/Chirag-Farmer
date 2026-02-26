package com.ayaan.chiragfarmer.di

import com.ayaan.chiragfarmer.data.remote.LocationApi
import com.ayaan.chiragfarmer.data.repository.LocationRepositoryImpl
import com.ayaan.chiragfarmer.domain.repository.LocationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {

    @Provides
    @Singleton
    fun provideLocationApi(okHttpClient: OkHttpClient): LocationApi {
        return Retrofit.Builder()
            .baseUrl(LocationApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(LocationApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLocationRepository(api: LocationApi): LocationRepository {
        return LocationRepositoryImpl(api)
    }
}
