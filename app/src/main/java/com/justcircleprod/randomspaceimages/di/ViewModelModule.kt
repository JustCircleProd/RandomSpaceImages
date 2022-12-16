package com.justcircleprod.randomspaceimages.di

import com.justcircleprod.randomspaceimages.data.remote.apod.APODAPI
import com.justcircleprod.randomspaceimages.data.remote.apod.APODConstants
import com.justcircleprod.randomspaceimages.data.remote.nasaLibrary.NASALibraryAPI
import com.justcircleprod.randomspaceimages.data.remote.nasaLibrary.NASALibraryConstants
import com.justcircleprod.randomspaceimages.data.repositories.apodRepository.DefaultAPODRepository
import com.justcircleprod.randomspaceimages.data.repositories.nasaLibraryRepository.DefaultNASALibraryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @Provides
    @ViewModelScoped
    fun provideNASALibraryAPI(): NASALibraryAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(NASALibraryConstants.BASE_URL)
            .build()
            .create(NASALibraryAPI::class.java)
    }

    @Provides
    @ViewModelScoped
    fun provideDefaultNASALibraryRepository(nasaLibraryAPI: NASALibraryAPI) =
        DefaultNASALibraryRepository(nasaLibraryAPI)

    @Provides
    @ViewModelScoped
    fun provideAPODAPI(): APODAPI {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.HEADERS
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(APODConstants.BASE_URL)
            .client(client)
            .build()
            .create(APODAPI::class.java)
    }

    @Provides
    @ViewModelScoped
    fun provideDefaultAPODRepository(apodAPI: APODAPI) =
        DefaultAPODRepository(apodAPI)
}