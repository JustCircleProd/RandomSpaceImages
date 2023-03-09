package com.justcircleprod.randomspaceimages.ui.common

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import com.bumptech.glide.load.resource.gif.GifDrawable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.nio.ByteBuffer
import java.util.*

fun getDisplayName(imageTitle: String?) =
    "${imageTitle ?: "random_space_images"}-${Calendar.getInstance().timeInMillis}"

suspend fun savePngToExternalStorage(
    context: Context,
    displayName: String,
    bitmap: Bitmap
): Boolean {
    return withContext(Dispatchers.IO) {
        val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$displayName.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.WIDTH, bitmap.width)
            put(MediaStore.Images.Media.HEIGHT, bitmap.height)
        }

        return@withContext try {
            val contentResolver = context.contentResolver
            contentResolver.insert(imageCollection, contentValues)?.also { uri ->
                contentResolver.openOutputStream(uri).use { outputStream ->
                    if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)) {
                        throw IOException("Couldn't save bitmap")
                    }
                }
            } ?: throw IOException("Couldn't create MediaStore entry")
            true
        } catch (e: IOException) {
            false
        }
    }
}

suspend fun saveGifToExternalStorage(
    context: Context,
    displayName: String,
    gifDrawable: GifDrawable
): Boolean {
    return withContext(Dispatchers.IO) {
        val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$displayName.gif")
            put(MediaStore.Images.Media.MIME_TYPE, "image/gif")
        }

        return@withContext try {
            val contentResolver = context.contentResolver
            contentResolver.insert(imageCollection, contentValues)?.also { uri ->
                contentResolver.openOutputStream(uri).use { outputStream ->
                    val bytes = ByteArray(gifDrawable.buffer.capacity())
                    (gifDrawable.buffer.duplicate().clear() as ByteBuffer).get(bytes)
                    outputStream?.write(bytes, 0, bytes.size)
                }
            } ?: throw IOException("Couldn't create MediaStore entry")
            true
        } catch (e: IOException) {
            false
        }
    }
}