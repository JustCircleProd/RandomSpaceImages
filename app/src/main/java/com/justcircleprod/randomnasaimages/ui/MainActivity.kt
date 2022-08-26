package com.justcircleprod.randomnasaimages.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.justcircleprod.randomnasaimages.data.models.ImageEntry
import com.justcircleprod.randomnasaimages.data.models.ImageEntryParamType
import com.justcircleprod.randomnasaimages.ui.detailImage.DetailImageScreen
import com.justcircleprod.randomnasaimages.ui.imageList.ImageListScreen
import com.justcircleprod.randomnasaimages.ui.theme.RandomNASAImagesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        const val IMAGE_LIST_SCREEN_DESTINATION = "image_list"

        const val DETAIL_IMAGE_SCREEN_DESTINATION = "detail_image"
        const val IMAGE_ENTRY_ARGUMENT_NAME = "IMAGE_ENTRY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RandomNASAImagesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = IMAGE_LIST_SCREEN_DESTINATION
                    ) {
                        composable(IMAGE_LIST_SCREEN_DESTINATION) {
                            ImageListScreen(navController = navController)
                        }
                        composable("$DETAIL_IMAGE_SCREEN_DESTINATION/{$IMAGE_ENTRY_ARGUMENT_NAME}", arguments = listOf(
                            navArgument(IMAGE_ENTRY_ARGUMENT_NAME) {
                                type = ImageEntryParamType()
                            }
                        )) {
                            val imageEntry = it.arguments?.getParcelable<ImageEntry>(
                                IMAGE_ENTRY_ARGUMENT_NAME
                            )
                            DetailImageScreen(navController = navController, imageEntry = imageEntry)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RandomNASAImagesTheme {
        
    }
}