package com.justcircleprod.randomspaceimages.data.repositories.apodRepository

import com.justcircleprod.randomspaceimages.data.models.APODEntry
import com.justcircleprod.randomspaceimages.util.Resource

interface APODRepository {
    suspend fun getTodayAPOD(): Resource<APODEntry>

    suspend fun getAPODsInDateRange(startDate: String, endDate: String): Resource<List<APODEntry>>
}