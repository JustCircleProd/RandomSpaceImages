package com.justcircleprod.randomspaceimages.ui.apod.apodPagesSnackbarState

sealed class APODPagesSnackBarState {
    data object FailedToOpenVideo : APODPagesSnackBarState()
    data object SavingImage : APODPagesSnackBarState()
    data object SuccessfullySavedImage : APODPagesSnackBarState()
    data object FailedToSaveImage : APODPagesSnackBarState()
    data object GrantPermission : APODPagesSnackBarState()
    data object PermissionIsRequired : APODPagesSnackBarState()
    data object PreparingToShare : APODPagesSnackBarState()
    data object FailedToShare : APODPagesSnackBarState()
    data object DownloadingLanguageModel : APODPagesSnackBarState()
    data object FailedToDownloadLanguageModel : APODPagesSnackBarState()
    data object Translating : APODPagesSnackBarState()
    data object FailedToTranslate : APODPagesSnackBarState()
}
