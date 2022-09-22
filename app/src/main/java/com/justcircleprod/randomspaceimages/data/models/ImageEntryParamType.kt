package com.justcircleprod.randomspaceimages.data.models

import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson
import com.justcircleprod.randomspaceimages.util.parcelable

class ImageEntryParamType : NavType<ImageEntry>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): ImageEntry? {
        return bundle.parcelable(key)
    }

    override fun parseValue(value: String): ImageEntry {
        return Gson().fromJson(value, ImageEntry::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: ImageEntry) {
        bundle.putParcelable(key, value)
    }
}