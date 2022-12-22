package com.justcircleprod.randomspaceimages.data.repositories.apodRepository

import com.justcircleprod.randomspaceimages.data.models.APODEntry
import com.justcircleprod.randomspaceimages.data.remote.apod.APODAPI
import com.justcircleprod.randomspaceimages.util.Resource
import javax.inject.Inject

class DefaultAPODRepository @Inject constructor(private val apodAPI: APODAPI) : APODRepository {
    override suspend fun getTodayAPOD(): Resource<APODEntry> {
        return try {
            Resource.Success(
                apodAPI.getTodayAPOD()
            )
        } catch (e: Exception) {
            Resource.Error(true)
        }
    }

    override suspend fun getAPODsInDateRange(
        startDate: String,
        endDate: String
    ): Resource<List<APODEntry>> {
        return try {
            Resource.Success(
                apodAPI.getAPODsInDateRange(startDate = startDate, endDate = endDate)
            )
        } catch (e: Exception) {
            Resource.Error(true)
        }
    }
}