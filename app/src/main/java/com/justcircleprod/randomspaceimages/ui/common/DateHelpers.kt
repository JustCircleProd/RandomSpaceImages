package com.justcircleprod.randomspaceimages.ui.common

import java.text.SimpleDateFormat
import java.util.*

fun fromServerFormatToAppFormat(dateStr: String, pattern: String): String? {
    val date = SimpleDateFormat(pattern, Locale.US)
        .parse(dateStr)

    return date?.let { SimpleDateFormat("dd.MM.yyyy", Locale.US).format(it) }
}