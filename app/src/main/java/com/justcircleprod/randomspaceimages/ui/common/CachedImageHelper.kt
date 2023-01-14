package com.justcircleprod.randomspaceimages.ui.common

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import com.justcircleprod.randomspaceimages.BuildConfig
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object CachedImageHelper {
    private const val CACHE_DIR_CHILD = "images"
    private const val CACHE_IMAGE_FOR_SHARE_NAME = "image.png"

    fun save(context: Context, bitmap: Bitmap): Boolean {
        return try {
            val cachePath = File(context.cacheDir, CACHE_DIR_CHILD)
            cachePath.mkdirs()

            val stream = FileOutputStream("$cachePath/$CACHE_IMAGE_FOR_SHARE_NAME")
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()

            true
        } catch (e: IOException) {
            false
        }

    }

    fun getImageUri(context: Context): Uri {
        val imagePath = File(context.cacheDir, CACHE_DIR_CHILD)
        val imageFile = File(imagePath, CACHE_IMAGE_FOR_SHARE_NAME)

        return FileProvider.getUriForFile(
            context,
            "${BuildConfig.APPLICATION_ID}.fileprovider",
            imageFile
        )
    }
}