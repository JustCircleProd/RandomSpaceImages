package com.justcircleprod.randomspaceimages.data.repositories.dataStoreRepository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun saveSetting(key: String, value: String)

    fun readSetting(key: String): Flow<String?>
}