package com.justcircleprod.randomnasaimages.data.repositories

import com.justcircleprod.randomnasaimages.data.remote.responses.NASAImagesList
import com.justcircleprod.randomnasaimages.util.Resource

interface NASALibraryRepository {
    suspend fun getImages(page: Int): Resource<NASAImagesList>
}