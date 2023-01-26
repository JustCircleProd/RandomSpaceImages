package com.justcircleprod.randomspaceimages.ui.more.licenses

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.ui.theme.LatoFontFamily

@Composable
@Suppress("UNCHECKED_CAST")
fun LicensesList(licenses: List<Map<String, Any>>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.license_list_vertical_arrangement)),
        modifier = Modifier.fillMaxWidth()
    ) {
        licenses.forEach { license ->
            LicenseTitle(
                licenseName = license["licenseName"] as String,
                licenseLink = license["licenseLink"] as String
            )

            for (packageInfo in (license["projects"] as List<Map<String, String>>)) {
                ProjectInfo(
                    projectNameVersion = packageInfo["projectNameVersion"]!!,
                    projectInfo = packageInfo["projectInfo"]!!
                )
            }
        }
    }
}

@Composable
private fun LicenseTitle(
    licenseName: String,
    licenseLink: String
) {
    val uriHandler = LocalUriHandler.current

    Text(
        text = licenseName,
        color = colorResource(id = R.color.primary),
        fontSize = 16.sp,
        textDecoration = TextDecoration.Underline,
        fontFamily = LatoFontFamily,
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(
                bounded = true,
                color = colorResource(id = R.color.ripple)
            )
        ) {
            uriHandler.openUri(licenseLink)
        }
    )
}

@Composable
private fun ProjectInfo(
    projectNameVersion: String,
    projectInfo: String,
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = projectNameVersion,
            color = colorResource(id = R.color.text),
            fontFamily = LatoFontFamily,
            fontSize = 15.sp
        )
        Text(
            text = projectInfo,
            color = colorResource(id = R.color.second_text),
            fontFamily = LatoFontFamily,
            fontSize = 14.sp
        )
    }
}