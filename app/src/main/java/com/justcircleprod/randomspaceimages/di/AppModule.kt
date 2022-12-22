package com.justcircleprod.randomspaceimages.di

import android.content.Context
import androidx.room.Room
import com.justcircleprod.randomspaceimages.data.dataStore.DataStoreManager
import com.justcircleprod.randomspaceimages.data.repositories.dataStoreRepository.DefaultDataStoreRepository
import com.justcircleprod.randomspaceimages.data.repositories.roomRepository.DefaultRoomRepository
import com.justcircleprod.randomspaceimages.data.room.database.AppDatabase
import com.justcircleprod.randomspaceimages.data.room.migrations.MIGRATION_1_2
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
        Room.databaseBuilder(context, AppDatabase::class.java, "RandomNASAImagesDatabase.db")
            .addMigrations(MIGRATION_1_2)
            .build()

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