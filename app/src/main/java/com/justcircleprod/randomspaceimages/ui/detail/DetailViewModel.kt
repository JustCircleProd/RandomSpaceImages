package com.justcircleprod.randomspaceimages.ui.detail

import com.justcircleprod.randomspaceimages.data.repositories.roomRepository.DefaultRoomRepository
import com.justcircleprod.randomspaceimages.ui.baseViewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    roomRepository: DefaultRoomRepository
) : BaseViewModel(roomRepository = roomRepository)