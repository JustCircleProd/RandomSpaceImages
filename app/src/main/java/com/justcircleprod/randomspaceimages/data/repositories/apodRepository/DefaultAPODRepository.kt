package com.justcircleprod.randomspaceimages.data.repositories.apodRepository

import com.justcircleprod.randomspaceimages.data.remote.apod.APODAPI
import com.justcircleprod.randomspaceimages.data.remote.apod.responses.APODItem
import com.justcircleprod.randomspaceimages.util.Resource
import javax.inject.Inject

class DefaultAPODRepository @Inject constructor(private val apodAPI: APODAPI) : APODRepository {
    override suspend fun getTodayAPOD(): Resource<APODItem> {
        return try {
            Resource.Success(
                apodAPI.getTodayAPOD()
            )
        } catch (e: Exception) {
            Resource.Error(true)
        }
    }
}