package com.justcircleprod.randomspaceimages.di

import android.content.Context
import com.justcircleprod.randomspaceimages.data.local.favourites.database.AppDatabase
import com.justcircleprod.randomspaceimages.data.local.settings.DataStoreManager
import com.justcircleprod.randomspaceimages.data.repository.APODFavouritesRepositoryImpl
import com.justcircleprod.randomspaceimages.data.repository.NASALibraryFavouritesRepositoryImpl
import com.justcircleprod.randomspaceimages.data.repository.SettingsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) =
        AppDatabase.getInstance(context)

    @Singleton
    @Provides
    fun provideAPODFavouritesRepository(appDatabase: AppDatabase) =
        APODFavouritesRepositoryImpl(appDatabase)

    @Singleton
    @Provides
    fun provideNASALibraryFavouritesRepository(appDatabase: AppDatabase) =
        NASALibraryFavouritesRepositoryImpl(appDatabase)

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context) =
        DataStoreManager(context)

    @Singleton
    @Provides
    fun provideSettingsRepository(dataStoreManager: DataStoreManager) =
        SettingsRepositoryImpl(dataStoreManager)
}