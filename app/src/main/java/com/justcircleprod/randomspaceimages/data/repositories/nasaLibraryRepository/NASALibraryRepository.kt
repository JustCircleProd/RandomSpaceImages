package com.justcircleprod.randomspaceimages.data.repositories.nasaLibraryRepository

import com.justcircleprod.randomspaceimages.data.remote.responses.NASAImagesList
import com.justcircleprod.randomspaceimages.util.Resource

interface NASALibraryRepository {
    suspend fun getImages(yearStart: Int, yearEnd: Int, page: Int): Resource<NASAImagesList>

    suspend fun searchImages(
        q: String,
        yearStart: Int,
        yearEnd: Int,
        page: Int
    ): Resource<NASAImagesList>
}