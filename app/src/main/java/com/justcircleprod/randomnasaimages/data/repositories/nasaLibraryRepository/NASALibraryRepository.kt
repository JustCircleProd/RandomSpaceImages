package com.justcircleprod.randomnasaimages.data.repositories.nasaLibraryRepository

import com.justcircleprod.randomnasaimages.data.remote.responses.NASAImagesList
import com.justcircleprod.randomnasaimages.util.Resource

interface NASALibraryRepository {
    suspend fun getImages(yearStart: Int, yearEnd: Int, page: Int): Resource<NASAImagesList>

    suspend fun searchImages(q: String, page: Int): Resource<NASAImagesList>
}