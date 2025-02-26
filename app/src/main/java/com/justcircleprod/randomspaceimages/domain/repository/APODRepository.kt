package com.justcircleprod.randomspaceimages.domain.repository

import com.justcircleprod.randomspaceimages.domain.model.APODEntry
import com.justcircleprod.randomspaceimages.util.Resource

interface APODRepository {
    suspend fun getTodayAPOD(): Resource<APODEntry>

    suspend fun getAPODsInDateRange(startDate: String, endDate: String): Resource<List<APODEntry>>

    suspend fun getAPODByDate(date: String): Resource<APODEntry>
}