package com.justcircleprod.randomnasaimages.data.repositories

import com.justcircleprod.randomnasaimages.data.remote.NASALibraryAPI
import com.justcircleprod.randomnasaimages.data.remote.responses.NASAImagesList
import com.justcircleprod.randomnasaimages.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class DefaultNASALibraryRepository @Inject constructor(private val nasaLibraryAPI: NASALibraryAPI) :
    NASALibraryRepository {

    override suspend fun getImages(page: Int): Resource<NASAImagesList> {
        return try {
            Resource.Success(nasaLibraryAPI.getImages(page = page))
        } catch (e: Exception) {
            Resource.Error("Error")
        }
    }
}