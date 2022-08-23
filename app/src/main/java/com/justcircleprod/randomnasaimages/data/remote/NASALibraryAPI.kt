package com.justcircleprod.randomnasaimages.data.remote

import com.justcircleprod.randomnasaimages.data.remote.responses.NASAImagesList
import retrofit2.http.GET
import retrofit2.http.Query

interface NASALibraryAPI {
    @GET
    suspend fun getImages(
        @Query("media_type") mediaType: String = "image",
        @Query("page") page: Int
    ): NASAImagesList
}