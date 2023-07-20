package com.justcircleprod.randomspaceimages.ui.apod.apodEntryItem

import com.justcircleprod.randomspaceimages.data.remote.apod.APODConstants
import com.justcircleprod.randomspaceimages.domain.model.APODEntry
import com.justcircleprod.randomspaceimages.ui.common.fromServerFormatToAppFormat
import kotlinx.coroutines.flow.MutableStateFlow

data class APODStates(
    val title: MutableStateFlow<String> = MutableStateFlow(""),
    val date: MutableStateFlow<String> = MutableStateFlow(""),
    val explanation: MutableStateFlow<String> = MutableStateFlow(""),
    val copyright: MutableStateFlow<String?> = MutableStateFlow("null"),
    val translating: MutableStateFlow<Boolean> = MutableStateFlow(false),
    val translated: MutableStateFlow<Boolean> = MutableStateFlow(false)
) {
    companion object {
        fun fromAPODEntry(apodEntry: APODEntry): APODStates {
            return APODStates(
                title = MutableStateFlow(apodEntry.title.trim()),
                date = MutableStateFlow(
                    fromServerFormatToAppFormat(
                        apodEntry.date,
                        APODConstants.DATE_FORMAT
                    ) ?: apodEntry.date
                ),
                explanation = MutableStateFlow(apodEntry.explanation.trim()),
                copyright = MutableStateFlow(
                    if (apodEntry.copyright != null) {
                        "Copyright: ${apodEntry.copyright.trim().replace("\n", "")}"
                    } else {
                        null
                    }
                ),
                translated = MutableStateFlow(false),
                translating = MutableStateFlow(false)
            )
        }
    }
}