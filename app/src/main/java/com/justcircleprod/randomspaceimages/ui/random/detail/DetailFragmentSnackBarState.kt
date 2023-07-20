package com.justcircleprod.randomspaceimages.ui.random.detail

sealed class DetailFragmentSnackBarState {
    object SavingImage : DetailFragmentSnackBarState()
    object SuccessfullySavedImage : DetailFragmentSnackBarState()
    object FailedToSaveImage : DetailFragmentSnackBarState()
    object GrantPermission : DetailFragmentSnackBarState()
    object PermissionIsRequired : DetailFragmentSnackBarState()
    object PreparingToShare : DetailFragmentSnackBarState()
    object FailedToShare : DetailFragmentSnackBarState()
    object DownloadingLanguageModel : DetailFragmentSnackBarState()
    object FailedToDownloadLanguageModel : DetailFragmentSnackBarState()
    object Translating : DetailFragmentSnackBarState()
    object FailedToTranslate : DetailFragmentSnackBarState()
}
