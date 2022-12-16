package com.justcircleprod.randomspaceimages.data.repositories.apodRepository

import com.justcircleprod.randomspaceimages.data.remote.apod.responses.APODItem
import com.justcircleprod.randomspaceimages.util.Resource

interface APODRepository {
    suspend fun getTodayAPOD(): Resource<APODItem>
}