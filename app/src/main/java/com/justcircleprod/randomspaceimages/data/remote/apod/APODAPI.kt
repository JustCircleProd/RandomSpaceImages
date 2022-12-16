package com.justcircleprod.randomspaceimages.data.remote.apod

import com.justcircleprod.randomspaceimages.data.remote.apod.responses.APODItem
import retrofit2.http.GET
import retrofit2.http.Query

interface APODAPI {
    @GET("apod")
    suspend fun getTodayAPOD(
        @Query("api_key") apiKey: String = APODConstants.API_KEY,
    ): APODItem
}