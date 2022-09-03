package com.justcircleprod.randomnasaimages.ui.detailImage

import com.justcircleprod.randomnasaimages.data.repositories.roomRepository.DefaultRoomRepository
import com.justcircleprod.randomnasaimages.ui.baseViewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(roomRepository: DefaultRoomRepository) :
    BaseViewModel(roomRepository = roomRepository)