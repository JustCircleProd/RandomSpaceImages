package com.justcircleprod.randomspaceimages.ui.apod.apodPagesSnackbarState

sealed class APODPagesSnackBarState {
    object FailedToOpenVideo : APODPagesSnackBarState()
    object SavingImage : APODPagesSnackBarState()
    object SuccessfullySavedImage : APODPagesSnackBarState()
    object FailedToSaveImage : APODPagesSnackBarState()
    object GrantPermission : APODPagesSnackBarState()
    object PermissionIsRequired : APODPagesSnackBarState()
    object PreparingToShare : APODPagesSnackBarState()
    object FailedToShare : APODPagesSnackBarState()
    object DownloadingLanguageModel : APODPagesSnackBarState()
    object FailedToDownloadLanguageModel : APODPagesSnackBarState()
    object Translating : APODPagesSnackBarState()
    object FailedToTranslate : APODPagesSnackBarState()
}
