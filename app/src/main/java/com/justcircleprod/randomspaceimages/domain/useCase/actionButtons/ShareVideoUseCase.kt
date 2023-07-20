package com.justcircleprod.randomspaceimages.domain.useCase.actionButtons

import android.content.Context
import android.content.Intent
import com.justcircleprod.randomspaceimages.R

class ShareVideoUseCase {
    operator fun invoke(context: Context, title: String, href: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                StringBuilder().apply {
                    append(
                        title,
                        '\n',
                        href,
                        "\n\n",
                        context.getString(R.string.watch_more_in_the_app)
                    )
                }.toString()
            )

            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }
}