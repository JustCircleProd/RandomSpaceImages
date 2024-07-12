package com.justcircleprod.randomspaceimages.ui.random.detail

import android.content.Context
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.justcircleprod.randomspaceimages.data.remote.nasaLibrary.NASALibraryConstants
import com.justcircleprod.randomspaceimages.data.repository.NASALibraryFavouritesRepositoryImpl
import com.justcircleprod.randomspaceimages.domain.model.NASALibraryImageEntry
import com.justcircleprod.randomspaceimages.domain.useCase.actionButtons.MyTranslator
import com.justcircleprod.randomspaceimages.domain.useCase.actionButtons.SaveImageUseCase
import com.justcircleprod.randomspaceimages.domain.useCase.actionButtons.ShareImage
import com.justcircleprod.randomspaceimages.ui.common.fromServerFormatToAppFormat
import com.justcircleprod.randomspaceimages.ui.random.randomBaseViewModel.RandomBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private data class LastTexts(
    var title: String,
    var description: String,
    var date: String,
    var additionalInfo: String
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    nasaLibraryFavouritesRepository: NASALibraryFavouritesRepositoryImpl,
    state: SavedStateHandle
) : RandomBaseViewModel(nasaLibraryFavouritesRepository = nasaLibraryFavouritesRepository) {
    private val nasaLibraryImageEntry =
        state.get<Parcelable>("nasaLibraryImageEntry") as NASALibraryImageEntry?

    val imageLoaded = MutableStateFlow(false)
    val title = MutableStateFlow("")
    val date = MutableStateFlow("")
    val description = MutableStateFlow("")
    val additionalInfo = MutableStateFlow("")

    val snackBarState = MutableStateFlow<DetailFragmentSnackBarState?>(null)


    lateinit var savingToGallery: MutableStateFlow<Boolean>
    lateinit var sharingImage: MutableStateFlow<Boolean>

    private val saveImageUseCase = SaveImageUseCase(
        onSaveFailed = {
            savingToGallery.value = false
            snackBarState.value = DetailFragmentSnackBarState.FailedToSaveImage
        },
        onSaved = {
            savingToGallery.value = false
            snackBarState.value = DetailFragmentSnackBarState.SuccessfullySavedImage
        }
    )

    private val shareImageUseCase = ShareImage(
        onShareReady = {
            sharingImage.value = false
            snackBarState.value = null
        },
        onShareFailed = {
            sharingImage.value = false
            snackBarState.value = DetailFragmentSnackBarState.FailedToShare
        }
    )


    private val myTranslator = MyTranslator(
        onFailedToDownloadLanguageModel = {
            snackBarState.value = DetailFragmentSnackBarState.FailedToDownloadLanguageModel
        }
    )

    private lateinit var downloadLanguageModelJob: Job

    val translating = MutableStateFlow(false)
    val translated = MutableStateFlow(false)

    // to store last texts: original or translated
    private lateinit var lastText: LastTexts


    init {
        if (nasaLibraryImageEntry != null) {
            initStartValues(
                titleValue = nasaLibraryImageEntry.title,
                dateValue = nasaLibraryImageEntry.dateCreated,
                descriptionValue = nasaLibraryImageEntry.description,
                centerValue = nasaLibraryImageEntry.center,
                photographerValue = nasaLibraryImageEntry.photographer,
                locationValue = nasaLibraryImageEntry.location,
                secondaryCreatorValue = nasaLibraryImageEntry.secondaryCreator
            )
        }
    }

    fun onEvent(event: DetailFragmentEvent) {
        when (event) {
            DetailFragmentEvent.OnFavouriteButtonClick -> {
                viewModelScope.launch(Dispatchers.IO) {
                    nasaLibraryImageEntry?.let {
                        if (nasaLibraryFavouritesRepository.isAddedToNASALibraryFavourites(it.nasaId)) {
                            nasaLibraryFavouritesRepository.removeFromNASALibraryFavourites(
                                nasaLibraryImageEntry
                            )
                        } else {
                            nasaLibraryFavouritesRepository.addToNASALibraryFavourites(
                                nasaLibraryImageEntry
                            )
                        }
                    }
                }
            }

            is DetailFragmentEvent.SaveImage -> {
                saveImage(event.context)
            }

            is DetailFragmentEvent.ShareImage -> {
                shareImage(event.context)
            }

            DetailFragmentEvent.Translate -> {
                translate()
            }
        }
    }

    private fun saveImage(context: Context) {
        nasaLibraryImageEntry?.let {
            if (savingToGallery.value) return

            savingToGallery.value = true
            snackBarState.value = DetailFragmentSnackBarState.SavingImage

            saveImageUseCase(context, viewModelScope, it.title, it.imageHref)
        }
    }

    private fun shareImage(context: Context) {
        nasaLibraryImageEntry?.let {
            if (sharingImage.value) return

            sharingImage.value = true
            snackBarState.value = DetailFragmentSnackBarState.PreparingToShare

            shareImageUseCase(context, viewModelScope, it.title, it.imageHref)
        }
    }

    private fun initStartValues(
        titleValue: String?, dateValue: String, descriptionValue: String?, centerValue: String?,
        photographerValue: String?, locationValue: String?, secondaryCreatorValue: String?
    ) {
        fun combineAdditionalInfo(
            center: String?,
            photographer: String?,
            location: String?,
            secondaryCreator: String?
        ): String {
            val additionalInfo = mutableListOf<String>()

            if (center != null) {
                additionalInfo.add("Center: ${center.trim()}")
            }

            if (photographer != null) {
                additionalInfo.add("Photographer: ${photographer.trim()}")
            }

            if (location != null) {
                additionalInfo.add("Location: ${location.trim()}")
            }

            if (secondaryCreator != null) {
                additionalInfo.add("Secondary creator: ${secondaryCreator.trim()}")
            }

            return additionalInfo.joinToString(separator = "\n")
        }


        title.value = titleValue?.trim() ?: ""

        date.value = fromServerFormatToAppFormat(
            dateValue,
            NASALibraryConstants.DATE_CREATED_FORMAT
        ) ?: ""

        description.value = descriptionValue?.trim() ?: ""

        additionalInfo.value = combineAdditionalInfo(
            centerValue,
            photographerValue,
            locationValue,
            secondaryCreatorValue
        )
    }

    private fun translate() {
        if (translating.value) return

        translating.value = true

        if (!myTranslator.readyToTranslate.value) {
            snackBarState.value = DetailFragmentSnackBarState.DownloadingLanguageModel

            // waiting for end of download language model
            downloadLanguageModelJob = viewModelScope.launch {
                myTranslator.readyToTranslate.collect {
                    if (it) {
                        translating.value = false
                        snackBarState.value = null
                        translate()
                        cancel()
                    }
                }
            }

            return
        }

        if (!translated.value && !::lastText.isInitialized) {
            snackBarState.value = DetailFragmentSnackBarState.Translating

            // remember last values
            lastText = LastTexts(
                title = title.value,
                date = date.value,
                description = description.value,
                additionalInfo = additionalInfo.value
            )

            translateValues()
            return
        }

        // if something in lastTexts just show it and remember last values
        swapLastTexts()

        translating.value = false
        translated.value = !translated.value
        snackBarState.value = null
    }

    private fun translateValues() {
        fun changeValuesOnTranslationEnd() {
            if (translating.value) {
                translating.value = false
            }
            if (!translated.value) {
                translated.value = true
            }
            snackBarState.value = null
        }


        myTranslator.translate(title.value)
            ?.addOnSuccessListener {
                title.value = it
                changeValuesOnTranslationEnd()
            }
            ?.addOnFailureListener {
                translating.value = false
                snackBarState.value = DetailFragmentSnackBarState.FailedToTranslate
            }

        myTranslator.translate(date.value)
            ?.addOnSuccessListener {
                date.value = it
                changeValuesOnTranslationEnd()
            }
            ?.addOnFailureListener {
                translating.value = false
                snackBarState.value = DetailFragmentSnackBarState.FailedToTranslate
            }

        myTranslator.translate(description.value)
            ?.addOnSuccessListener {
                description.value = it
                changeValuesOnTranslationEnd()
            }
            ?.addOnFailureListener {
                translating.value = false
                snackBarState.value = DetailFragmentSnackBarState.FailedToTranslate
            }

        myTranslator.translate(additionalInfo.value)
            ?.addOnSuccessListener {
                additionalInfo.value = it
                changeValuesOnTranslationEnd()
            }
            ?.addOnFailureListener {
                translating.value = false
                snackBarState.value = DetailFragmentSnackBarState.FailedToTranslate
            }
    }

    private fun swapLastTexts() {
        val titleValue = lastText.title
        val dateValue = lastText.date
        val descriptionValue = lastText.description
        val additionalInfoValue = lastText.additionalInfo

        lastText.title = title.value
        lastText.date = date.value
        lastText.description = description.value
        lastText.additionalInfo = additionalInfo.value

        title.value = titleValue
        date.value = dateValue
        description.value = descriptionValue
        additionalInfo.value = additionalInfoValue
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