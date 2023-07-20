package com.justcircleprod.randomspaceimages.domain.useCase.actionButtons

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.justcircleprod.randomspaceimages.BuildConfig
import com.justcircleprod.randomspaceimages.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer

class ShareImage(
    private val onShareFailed: () -> Unit,
    private val onShareReady: () -> Unit
) {
    operator fun invoke(
        context: Context,
        viewModelScope: CoroutineScope,
        title: String?,
        href: String
    ) {
        Glide.with(context).load(href)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    onShareFailed()
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    viewModelScope.launch {
                        resource?.let {
                            val uri = if (href.endsWith(".gif")) {
                                CachedImageHelper.saveGif(context, it as GifDrawable)
                            } else {
                                CachedImageHelper.savePng(context, it.toBitmap())
                            }

                            if (uri == null) {
                                onShareFailed()
                                return@launch
                            }

                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND

                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    StringBuilder().apply {
                                        if (title != null) {
                                            append(title, "\n\n")
                                        }
                                        append(context.getString(R.string.watch_more_in_the_app))
                                    }.toString()
                                )

                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                                val imageType = if (href.endsWith(".gif")) "gif" else "png"

                                clipData = ClipData(
                                    "Image",
                                    arrayOf("image/$imageType"),
                                    ClipData.Item(uri)
                                )

                                putExtra(
                                    Intent.EXTRA_STREAM,
                                    uri
                                )
                                type = "image/$imageType"
                            }

                            val shareIntent = Intent.createChooser(sendIntent, null)
                            context.startActivity(shareIntent)

                            onShareReady()
                        }
                    }

                    return true
                }
            }).submit()
    }
}

private object CachedImageHelper {
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