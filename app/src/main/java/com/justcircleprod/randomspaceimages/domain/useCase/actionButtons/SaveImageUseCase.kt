package com.justcircleprod.randomspaceimages.domain.useCase.actionButtons

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.provider.MediaStore
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.nio.ByteBuffer
import java.util.Calendar

class SaveImageUseCase(
    private val onSaved: () -> Unit,
    private val onSaveFailed: () -> Unit,
) {
    operator fun invoke(
        context: Context,
        viewModelScope: CoroutineScope,
        title: String?,
        href: String
    ) {
        Glide
            .with(context)
            .load(href)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    onSaveFailed()
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    viewModelScope.launch {
                        resource.let { imageDrawable ->
                            val savedSuccessfully = if (href.endsWith(".gif")) {
                                saveGifToExternalStorage(
                                    context,
                                    getDisplayName(title),
                                    imageDrawable as GifDrawable
                                )
                            } else {
                                savePngToExternalStorage(
                                    context,
                                    getDisplayName(title),
                                    imageDrawable.toBitmap()
                                )
                            }
                            if (savedSuccessfully) {
                                onSaved()
                            } else {
                                onSaveFailed()
                            }
                        }
                    }
                    return true
                }
            })
            .submit()
    }

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
                        outputStream?.let {
                            if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)) {
                                throw IOException("Couldn't save bitmap")
                            }
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
}