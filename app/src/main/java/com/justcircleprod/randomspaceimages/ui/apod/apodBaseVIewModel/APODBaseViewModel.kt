package com.justcircleprod.randomspaceimages.ui.apod.apodBaseVIewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justcircleprod.randomspaceimages.data.local.settings.DataStoreConstants
import com.justcircleprod.randomspaceimages.domain.repository.APODFavouritesRepository
import com.justcircleprod.randomspaceimages.domain.repository.SettingsRepository
import com.justcircleprod.randomspaceimages.domain.useCase.actionButtons.MyTranslator
import com.justcircleprod.randomspaceimages.domain.useCase.actionButtons.SaveImageUseCase
import com.justcircleprod.randomspaceimages.domain.useCase.actionButtons.ShareImage
import com.justcircleprod.randomspaceimages.domain.useCase.actionButtons.ShareVideoUseCase
import com.justcircleprod.randomspaceimages.ui.apod.apodEntryItem.APODStates
import com.justcircleprod.randomspaceimages.ui.apod.apodPagesSnackbarState.APODPagesSnackBarState
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private data class LastText(
    var title: String,
    var explanation: String,
    var date: String,
    var copyright: String?,
)

abstract class APODBaseViewModel(
    protected val apodFavouritesRepository: APODFavouritesRepository,
    settingsRepository: SettingsRepository
) : ViewModel() {
    val apodsStates = MutableStateFlow(mutableListOf<APODStates>())

    val snackBarState = MutableStateFlow<APODPagesSnackBarState?>(null)


    fun isAddedToFavouritesLiveData(date: String) =
        apodFavouritesRepository.isAddedToAPODFavouritesLiveData(date)


    lateinit var savingToGallery: MutableStateFlow<Boolean>
    lateinit var sharingImage: MutableStateFlow<Boolean>

    private val qualityOfSavingAndSharingImages =
        settingsRepository.readSetting(DataStoreConstants.QUALITY_OF_SAVING_AND_SHARING_IMAGES)

    private val saveImageUseCase = SaveImageUseCase(
        onSaveFailed = {
            savingToGallery.value = false
            snackBarState.value = APODPagesSnackBarState.FailedToSaveImage
        },
        onSaved = {
            savingToGallery.value = false
            snackBarState.value = APODPagesSnackBarState.SuccessfullySavedImage
        }
    )

    private val shareImageUseCase = ShareImage(
        onShareReady = {
            sharingImage.value = false
            snackBarState.value = null
        },
        onShareFailed = {
            sharingImage.value = false
            snackBarState.value = APODPagesSnackBarState.FailedToShare
        }
    )

    private val shareVideoUseCase = ShareVideoUseCase()


    private val myTranslator = MyTranslator(
        onFailedToDownloadLanguageModel = {
            snackBarState.value = APODPagesSnackBarState.FailedToDownloadLanguageModel
        }
    )

    private lateinit var downloadLanguageModelJob: Job

    // store last texts: original or translated
    // index in apodsStates and LastTexts
    private val lastTexts = mutableListOf<Pair<Int, LastText>>()

    protected fun saveImage(context: Context, title: String, url: String, hdurl: String?) {
        if (savingToGallery.value) return

        savingToGallery.value = true
        snackBarState.value = APODPagesSnackBarState.SavingImage

        viewModelScope.launch {
            val imageHref =
                if (hdurl != null && qualityOfSavingAndSharingImages.first() == DataStoreConstants.HIGH_QUALITY) {
                    hdurl
                } else {
                    url
                }

            saveImageUseCase(context, viewModelScope, title, imageHref)
        }
    }

    protected fun shareImage(context: Context, title: String, url: String, hdurl: String?) {
        if (sharingImage.value) return

        sharingImage.value = true
        snackBarState.value = APODPagesSnackBarState.PreparingToShare

        viewModelScope.launch {
            val imageHref =
                if (hdurl != null && qualityOfSavingAndSharingImages.first() == DataStoreConstants.HIGH_QUALITY) {
                    hdurl
                } else {
                    url
                }
            shareImageUseCase(context, viewModelScope, title, imageHref)
        }
    }

    protected fun shareVideo(context: Context, title: String, url: String) {
        if (sharingImage.value) return

        sharingImage.value = true
        snackBarState.value = APODPagesSnackBarState.PreparingToShare

        shareVideoUseCase(context, title, url)
    }

    protected fun translate(apodIndex: Int) {
        if (apodsStates.value[apodIndex].translating.value) return

        apodsStates.value[apodIndex].translating.value = true

        if (!myTranslator.readyToTranslate.value) {
            snackBarState.value = APODPagesSnackBarState.DownloadingLanguageModel

            // waiting for end of download language model
            downloadLanguageModelJob = viewModelScope.launch {
                myTranslator.readyToTranslate.collect {
                    if (it) {
                        apodsStates.value[apodIndex].translating.value = false
                        snackBarState.value = null
                        translate(apodIndex)
                        cancel()
                    }
                }
            }

            return
        }

        // if not translated and has not been translated before
        if (!apodsStates.value[apodIndex].translated.value && !lastTexts.any { it.first == apodIndex }) {
            snackBarState.value = APODPagesSnackBarState.Translating

            // remember last values
            lastTexts.add(
                Pair(
                    apodIndex,
                    LastText(
                        title = apodsStates.value[apodIndex].title.value,
                        date = apodsStates.value[apodIndex].date.value,
                        explanation = apodsStates.value[apodIndex].explanation.value,
                        copyright = apodsStates.value[apodIndex].copyright.value
                    )
                )

            )

            translateValues(apodIndex)
            return
        }

        // if something in lastTexts just show it and remember last values
        swapLastTexts(apodIndex)

        apodsStates.value[apodIndex].translated.value =
            !apodsStates.value[apodIndex].translated.value
        apodsStates.value[apodIndex].translating.value = false
        snackBarState.value = null
    }

    protected fun clearLastTexts() {
        lastTexts.clear()
    }

    private fun translateValues(apodIndex: Int) {
        fun changeValuesOnTranslationEnd() {
            if (!apodsStates.value[apodIndex].translated.value) {
                apodsStates.value[apodIndex].translated.value = true
            }
            if (apodsStates.value[apodIndex].translating.value) {
                apodsStates.value[apodIndex].translating.value = false
            }
            snackBarState.value = null
        }

        myTranslator.translate(apodsStates.value[apodIndex].title.value)
            ?.addOnSuccessListener {
                apodsStates.value[apodIndex].title.value = it
                changeValuesOnTranslationEnd()
            }
            ?.addOnFailureListener {
                apodsStates.value[apodIndex].translating.value = false
                snackBarState.value = APODPagesSnackBarState.FailedToTranslate
            }

        myTranslator.translate(apodsStates.value[apodIndex].date.value)
            ?.addOnSuccessListener {
                apodsStates.value[apodIndex].date.value = it
                changeValuesOnTranslationEnd()
            }
            ?.addOnFailureListener {
                apodsStates.value[apodIndex].translating.value = false
                snackBarState.value = APODPagesSnackBarState.FailedToTranslate
            }

        myTranslator.translate(apodsStates.value[apodIndex].explanation.value)
            ?.addOnSuccessListener {
                apodsStates.value[apodIndex].explanation.value = it
                changeValuesOnTranslationEnd()
            }
            ?.addOnFailureListener {
                apodsStates.value[apodIndex].translating.value = false
                snackBarState.value = APODPagesSnackBarState.FailedToTranslate
            }

        if (apodsStates.value[apodIndex].copyright.value != null) {
            myTranslator.translate(apodsStates.value[apodIndex].copyright.value!!)
                ?.addOnSuccessListener {
                    apodsStates.value[apodIndex].copyright.value = it
                    changeValuesOnTranslationEnd()
                }
                ?.addOnFailureListener {
                    apodsStates.value[apodIndex].translating.value = false
                    snackBarState.value = APODPagesSnackBarState.FailedToTranslate
                }
        }
    }

    private fun swapLastTexts(apodIndex: Int) {
        val lastText = lastTexts.find { it.first == apodIndex } ?: return

        val titleValue = lastText.second.title
        val dateValue = lastText.second.date
        val descriptionValue = lastText.second.explanation
        val additionalInfoValue = lastText.second.copyright

        lastText.second.title = apodsStates.value[apodIndex].title.value
        lastText.second.date = apodsStates.value[apodIndex].date.value
        lastText.second.explanation = apodsStates.value[apodIndex].explanation.value
        lastText.second.copyright = apodsStates.value[apodIndex].copyright.value

        apodsStates.value[apodIndex].title.value = titleValue
        apodsStates.value[apodIndex].date.value = dateValue
        apodsStates.value[apodIndex].explanation.value = descriptionValue
        apodsStates.value[apodIndex].copyright.value = additionalInfoValue
    }

    override fun onCleared() {
        super.onCleared()

        savingToGallery.value = false
        sharingImage.value = false

        if (::downloadLanguageModelJob.isInitialized && !downloadLanguageModelJob.isCancelled) {
            downloadLanguageModelJob.cancel()
        }
        myTranslator.closeTranslator()
    }
}