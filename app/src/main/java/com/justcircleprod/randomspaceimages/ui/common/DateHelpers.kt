package com.justcircleprod.randomspaceimages.ui.common

import java.text.SimpleDateFormat
import java.util.Locale

fun fromServerFormatToAppFormat(dateStr: String, pattern: String): String? {
    val date = SimpleDateFormat(pattern, Locale.US)
        .parse(dateStr)

    return date?.let { SimpleDateFormat("dd MMMM yyyy", Locale.US).format(it) }
}