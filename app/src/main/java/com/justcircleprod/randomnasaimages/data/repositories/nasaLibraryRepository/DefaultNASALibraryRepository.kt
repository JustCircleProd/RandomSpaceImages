package com.justcircleprod.randomnasaimages.data.repositories.nasaLibraryRepository

import com.justcircleprod.randomnasaimages.data.remote.NASALibraryAPI
import com.justcircleprod.randomnasaimages.data.remote.responses.NASAImagesList
import com.justcircleprod.randomnasaimages.util.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class DefaultNASALibraryRepository @Inject constructor(private val nasaLibraryAPI: NASALibraryAPI) :
    NASALibraryRepository {

    override suspend fun getImages(
        yearStart: Int,
        yearEnd: Int,
        page: Int
    ): Resource<NASAImagesList> {
        return try {
            Resource.Success(
                nasaLibraryAPI.getImages(
                    page = page,
                    yearStart = yearStart,
                    yearEnd = yearEnd
                )
            )
        } catch (e: Exception) {
            Resource.Error(true)
        }
    }

    override suspend fun searchImages(
        q: String,
        page: Int,
    ): Resource<NASAImagesList> {
        return try {
            Resource.Success(
                nasaLibraryAPI.searchImages(
                    q = q,
                    page = page
                )
            )
        } catch (e: Exception) {
            Resource.Error(true)
        }
    }
}