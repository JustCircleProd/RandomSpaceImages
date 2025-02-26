package com.justcircleprod.randomspaceimages.data.repository

import com.justcircleprod.randomspaceimages.data.remote.nasaLibrary.NASALibraryAPI
import com.justcircleprod.randomspaceimages.data.remote.nasaLibrary.responses.NASALibraryImageList
import com.justcircleprod.randomspaceimages.domain.repository.NASALibraryRepository
import com.justcircleprod.randomspaceimages.util.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class NASALibraryRepositoryImpl @Inject constructor(private val nasaLibraryAPI: NASALibraryAPI) :
    NASALibraryRepository {

    override suspend fun getImages(
        yearStart: Int,
        yearEnd: Int,
        page: Int
    ): Resource<NASALibraryImageList> {
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
        yearStart: Int,
        yearEnd: Int,
        page: Int,
    ): Resource<NASALibraryImageList> {
        return try {
            Resource.Success(
                nasaLibraryAPI.searchImages(
                    q = q,
                    yearStart = yearStart,
                    yearEnd = yearEnd,
                    page = page
                )
            )
        } catch (e: Exception) {
            Resource.Error(true)
        }
    }
}