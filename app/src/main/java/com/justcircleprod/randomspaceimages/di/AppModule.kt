package com.justcircleprod.randomspaceimages.di

import android.content.Context
import com.justcircleprod.randomspaceimages.data.dataStore.DataStoreManager
import com.justcircleprod.randomspaceimages.data.repositories.dataStoreRepository.DefaultDataStoreRepository
import com.justcircleprod.randomspaceimages.data.repositories.roomRepository.DefaultRoomRepository
import com.justcircleprod.randomspaceimages.data.room.database.AppDatabase
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
    fun provideRoomDatabase(@ApplicationContext context: Context) =
        AppDatabase.getInstance(context)

    @Singleton
    @Provides
    fun provideDefaultRoomRepository(roomDatabase: AppDatabase) =
        DefaultRoomRepository(roomDatabase)

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context) =
        DataStoreManager(context)

    @Singleton
    @Provides
    fun provideDefaultDataStoreRepository(dataStoreManager: DataStoreManager) =
        DefaultDataStoreRepository(dataStoreManager)
}