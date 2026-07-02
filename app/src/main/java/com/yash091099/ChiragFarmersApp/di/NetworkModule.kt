package com.yash091099.ChiragFarmersApp.di

import com.yash091099.ChiragFarmersApp.BuildConfig
import com.yash091099.ChiragFarmersApp.data.remote.AuthInterceptor
import com.yash091099.ChiragFarmersApp.data.remote.TokenRefreshAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.siddharthjaswal.logpose.LogPoseConfig
import io.github.siddharthjaswal.logpose.LogPoseInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseOkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

//    private const val BASE_URL = "https://rheumatoid-ringlike-al.ngrok-free.dev/"
    private const val BASE_URL = BuildConfig.BASE_URL

    @Provides
    @BaseOkHttpClient
    @Singleton
    fun provideBaseOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(LogPoseInterceptor(LogPoseConfig(enabled = BuildConfig.DEBUG)))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        authenticator: TokenRefreshAuthenticator,
        @BaseOkHttpClient baseClient: OkHttpClient
    ): OkHttpClient {
        return baseClient.newBuilder()
            .addInterceptor(authInterceptor)
            .authenticator(authenticator)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
}