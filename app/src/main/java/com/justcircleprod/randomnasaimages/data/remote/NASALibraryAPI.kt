package com.justcircleprod.randomnasaimages.data.remote

import com.justcircleprod.randomnasaimages.data.remote.responses.NASAImagesList
import retrofit2.http.GET
import retrofit2.http.Query

interface NASALibraryAPI {
    @GET("search")
    suspend fun getImages(
        @Query("media_type") mediaType: String = "image",
        @Query("year_start") yearStart: Int,
        @Query("year_end") yearEnd: Int,
        @Query("page") page: Int
    ): NASAImagesList

    @GET("search")
    suspend fun searchImages(
        @Query("media_type") mediaType: String = "image",
        @Query("q") q: String,
        @Query("page") page: Int
    ): NASAImagesList
}