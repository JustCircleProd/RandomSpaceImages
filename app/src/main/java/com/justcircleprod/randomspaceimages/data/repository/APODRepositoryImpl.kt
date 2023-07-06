package com.justcircleprod.randomspaceimages.data.repository

import com.justcircleprod.randomspaceimages.data.remote.apod.APODAPI
import com.justcircleprod.randomspaceimages.domain.model.APODEntry
import com.justcircleprod.randomspaceimages.domain.repository.APODRepository
import com.justcircleprod.randomspaceimages.util.Resource
import javax.inject.Inject

class APODRepositoryImpl @Inject constructor(private val apodAPI: APODAPI) : APODRepository {
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

    override suspend fun getAPODByDate(date: String): Resource<APODEntry> {
        return try {
            Resource.Success(apodAPI.getAPODByDate(date))
        } catch (e: Exception) {
            Resource.Error(true)
        }
    }
}