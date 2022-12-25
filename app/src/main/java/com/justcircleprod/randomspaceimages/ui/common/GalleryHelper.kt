package com.justcircleprod.randomspaceimages.ui.common

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.provider.MediaStore
import androidx.compose.runtime.MutableState
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.io.IOException
import java.util.*


enum class SaveState {
    NOT_SAVED, SAVING, SAVED
}


fun saveToGallery(
    context: Context,
    imageTitle: String?,
    imageHref: String,
    onSaved: () -> Unit
) {
    Glide
        .with(context)
        .load(imageHref)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                return true
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                resource?.let { imageDrawable ->
                    val savedSuccessfully =
                        saveToExternalStorage(
                            context,
                            getDisplayName(imageTitle),
                            imageDrawable.toBitmap()
                        )
                    if (savedSuccessfully) {
                        onSaved()
                    }
                }
                return true
            }
        })
        .submit()
}

private fun getDisplayName(imageTitle: String?) =
    "${imageTitle ?: "random_space_images"}-${Calendar.getInstance().timeInMillis}"

private fun saveToExternalStorage(context: Context, displayName: String, bitmap: Bitmap): Boolean {
    val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "$displayName.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        put(MediaStore.Images.Media.WIDTH, bitmap.width)
        put(MediaStore.Images.Media.HEIGHT, bitmap.height)
    }

    return try {
        val contentResolver = context.contentResolver
        contentResolver.insert(imageCollection, contentValues)?.also { uri ->
            contentResolver.openOutputStream(uri).use { outputStream ->
                if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)) {
                    throw IOException("Couldn't save bitmap")
                }
            }
        } ?: throw IOException("Couldn't create MediaStore entry")
        true
    } catch (e: IOException) {
        false
    }
}