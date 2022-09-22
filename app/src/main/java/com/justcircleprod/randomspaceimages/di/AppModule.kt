package com.justcircleprod.randomspaceimages.di

import android.content.Context
import androidx.room.Room
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
        Room.databaseBuilder(context, AppDatabase::class.java, "RandomNASAImagesDatabase.db")
            .build()

    @Singleton
    @Provides
    fun provideDefaultRoomRepository(roomDatabase: AppDatabase) =
        DefaultRoomRepository(roomDatabase)
}