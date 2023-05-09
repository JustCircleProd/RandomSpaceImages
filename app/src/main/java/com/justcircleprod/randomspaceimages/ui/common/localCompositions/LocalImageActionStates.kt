package com.justcircleprod.randomspaceimages.ui.common.localCompositions

import androidx.compose.runtime.compositionLocalOf
import kotlinx.coroutines.flow.MutableStateFlow

data class ImageActionStates(
    val savingToGallery: MutableStateFlow<Boolean> = MutableStateFlow(false),
    val sharingImage: MutableStateFlow<Boolean> = MutableStateFlow(false)
)

val LocalImageActionStates = compositionLocalOf { ImageActionStates() }