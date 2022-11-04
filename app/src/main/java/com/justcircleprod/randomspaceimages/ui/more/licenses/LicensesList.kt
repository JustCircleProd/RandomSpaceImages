package com.justcircleprod.randomspaceimages.ui.more.licenses

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.justcircleprod.randomspaceimages.R

@Composable
@Suppress("UNCHECKED_CAST")
fun LicensesList(licenses: List<Map<String, Any>>, modifier: Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.license_list_vertical_arrangement)),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.more_card_horizontal_space_size))
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
        style = TextStyle(
            fontSize = 16.sp,
            color = MaterialTheme.colors.primary,
            textDecoration = TextDecoration.Underline
        ),
        modifier = Modifier.clickable {
            uriHandler.openUri(licenseLink)
        })
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
            fontSize = 15.sp
        )
        Text(
            text = projectInfo,
            fontSize = 14.sp
        )
    }
}