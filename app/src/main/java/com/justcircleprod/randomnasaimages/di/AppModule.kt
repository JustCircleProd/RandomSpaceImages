package com.justcircleprod.randomnasaimages.di

import com.justcircleprod.randomnasaimages.data.remote.NASALibraryAPI
import com.justcircleprod.randomnasaimages.data.remote.RemoteConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideNASALibraryAPI(): NASALibraryAPI {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(RemoteConstants.NASA_LIBRARY_BASE_URL)
            .client(client)
            .build()
            .create(NASALibraryAPI::class.java)
    }
}