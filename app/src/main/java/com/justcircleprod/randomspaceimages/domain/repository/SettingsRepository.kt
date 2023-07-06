package com.justcircleprod.randomspaceimages.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun saveSetting(key: String, value: String)

    fun readSetting(key: String): Flow<String?>
}