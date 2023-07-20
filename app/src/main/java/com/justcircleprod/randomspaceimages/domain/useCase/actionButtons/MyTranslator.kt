package com.justcircleprod.randomspaceimages.domain.useCase.actionButtons

import com.google.android.gms.tasks.Task
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Locale

class MyTranslator(
    private val onFailedToDownloadLanguageModel: () -> Unit
) {
    private lateinit var translator: Translator
    val readyToTranslate = MutableStateFlow(false)

    init {
        val translateLanguage = TranslateLanguage.fromLanguageTag(Locale.getDefault().language)

        if (translateLanguage != null) {
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(translateLanguage)
                .build()
            translator = Translation.getClient(options)

            val conditions = DownloadConditions.Builder().build()

            translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener {
                    readyToTranslate.value = true
                }
                .addOnFailureListener {
                    onFailedToDownloadLanguageModel()
                }
        }
    }

    fun translate(text: String): Task<String>? {
        if (!readyToTranslate.value) return null

        return translator.translate(text)
    }

    fun closeTranslator() {
        translator.close()
    }
}