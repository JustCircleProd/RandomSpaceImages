package com.justcircleprod.randomspaceimages.ui.common

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.justcircleprod.randomspaceimages.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer

object CachedImageHelper {
    private const val CACHE_DIR_CHILD = "images"
    private const val CACHE_IMAGE_FOR_SHARE_NAME = "image"

    suspend fun savePng(context: Context, bitmap: Bitmap): Uri? {
        return withContext(Dispatchers.IO) {
            try {
                val cachePath = File(context.cacheDir, CACHE_DIR_CHILD)
                cachePath.mkdirs()

                val stream = FileOutputStream("$cachePath/$CACHE_IMAGE_FOR_SHARE_NAME.png")
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                stream.close()

                val imagePath = File(context.cacheDir, CACHE_DIR_CHILD)
                val imageFile = File(imagePath, "$CACHE_IMAGE_FOR_SHARE_NAME.png")

                FileProvider.getUriForFile(
                    context,
                    "${BuildConfig.APPLICATION_ID}.fileprovider",
                    imageFile
                )
            } catch (e: IOException) {
                null
            }
        }
    }

    suspend fun saveGif(context: Context, gifDrawable: GifDrawable): Uri? {
        return withContext(Dispatchers.IO) {
            try {
                val cachePath = File(context.cacheDir, CACHE_DIR_CHILD)
                cachePath.mkdirs()

                val stream = FileOutputStream("$cachePath/$CACHE_IMAGE_FOR_SHARE_NAME.gif")
                val bytes = ByteArray(gifDrawable.buffer.capacity())
                (gifDrawable.buffer.duplicate().clear() as ByteBuffer).get(bytes)
                stream.write(bytes, 0, bytes.size)
                stream.close()

                val imagePath = File(context.cacheDir, CACHE_DIR_CHILD)
                val imageFile = File(imagePath, "$CACHE_IMAGE_FOR_SHARE_NAME.gif")

                FileProvider.getUriForFile(
                    context,
                    "${BuildConfig.APPLICATION_ID}.fileprovider",
                    imageFile
                )
            } catch (e: IOException) {
                null
            }
        }
    }
}