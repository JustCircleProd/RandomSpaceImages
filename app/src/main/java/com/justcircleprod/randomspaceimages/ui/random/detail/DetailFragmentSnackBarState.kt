package com.justcircleprod.randomspaceimages.ui.random.detail

sealed class DetailFragmentSnackBarState {
    data object SavingImage : DetailFragmentSnackBarState()
    data object SuccessfullySavedImage : DetailFragmentSnackBarState()
    data object FailedToSaveImage : DetailFragmentSnackBarState()
    data object GrantPermission : DetailFragmentSnackBarState()
    data object PermissionIsRequired : DetailFragmentSnackBarState()
    data object PreparingToShare : DetailFragmentSnackBarState()
    data object FailedToShare : DetailFragmentSnackBarState()
    data object DownloadingLanguageModel : DetailFragmentSnackBarState()
    data object FailedToDownloadLanguageModel : DetailFragmentSnackBarState()
    data object Translating : DetailFragmentSnackBarState()
    data object FailedToTranslate : DetailFragmentSnackBarState()
}
