package com.justcircleprod.randomspaceimages.ui.random.detail

import com.justcircleprod.randomspaceimages.data.repositories.roomRepository.DefaultRoomRepository
import com.justcircleprod.randomspaceimages.ui.random.nasaLibraryBaseViewModel.NASALibraryBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    roomRepository: DefaultRoomRepository
) : NASALibraryBaseViewModel(roomRepository = roomRepository)