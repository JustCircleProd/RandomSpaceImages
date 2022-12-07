package com.justcircleprod.randomspaceimages.di

import com.justcircleprod.randomspaceimages.data.remote.nasaLibrary.NASALibraryAPI
import com.justcircleprod.randomspaceimages.data.remote.nasaLibrary.NASALibraryConstants
import com.justcircleprod.randomspaceimages.data.repositories.nasaLibraryRepository.DefaultNASALibraryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
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
}