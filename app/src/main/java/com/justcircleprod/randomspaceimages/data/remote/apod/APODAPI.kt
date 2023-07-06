package com.justcircleprod.randomspaceimages.data.remote.apod

import com.justcircleprod.randomspaceimages.domain.model.APODEntry
import retrofit2.http.GET
import retrofit2.http.Query

interface APODAPI {
    @GET("apod")
    suspend fun getTodayAPOD(
        @Query("api_key") apiKey: String = APODConstants.API_KEY,
    ): APODEntry

    @GET("apod")
    suspend fun getAPODsInDateRange(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String = APODConstants.API_KEY
    ): List<APODEntry>

    @GET("apod")
    suspend fun getAPODByDate(
        @Query("date") date: String,
        @Query("api_key") apiKey: String = APODConstants.API_KEY
    ): APODEntry
}