package com.justcircleprod.randomnasaimages.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.justcircleprod.randomnasaimages.R
import com.justcircleprod.randomnasaimages.data.models.ImageEntry
import com.justcircleprod.randomnasaimages.data.models.ImageEntryParamType
import com.justcircleprod.randomnasaimages.ui.bottomNavItem.BottomNavItem
import com.justcircleprod.randomnasaimages.ui.detailImage.DetailImageScreen
import com.justcircleprod.randomnasaimages.ui.favourites.FavouritesScreen
import com.justcircleprod.randomnasaimages.ui.home.HomeScreen
import com.justcircleprod.randomnasaimages.ui.screen.Screen
import com.justcircleprod.randomnasaimages.ui.search.SearchScreen
import com.justcircleprod.randomnasaimages.ui.theme.RandomNASAImagesTheme
import com.yandex.mobile.ads.common.MobileAds
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) { }

        setContent {
            RandomNASAImagesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    val bottomNavigationState = remember { MutableTransitionState(true) }

                    ConstraintLayout(modifier = Modifier.padding()) {
                        NavHost(
                            navController = navController,
                            bottomNavigationState = bottomNavigationState
                        )

                        val bottomNavigation = createRef()

                        BottomNavigation(
                            navController = navController,
                            bottomNavigationState = bottomNavigationState,
                            modifier = Modifier
                                .constrainAs(bottomNavigation) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(parent.bottom, margin = 10.dp)
                                }
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun NavHost(
        navController: NavHostController,
        bottomNavigationState: MutableTransitionState<Boolean>
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(Screen.Home.route) {
                bottomNavigationState.targetState = true
                HomeScreen(
                    navController = navController
                )
            }
            composable(Screen.Search.route) {
                bottomNavigationState.targetState = true
                SearchScreen(navController = navController)
            }
            composable(Screen.Favourites.route) {
                bottomNavigationState.targetState = true
                FavouritesScreen(navController = navController)
            }
            composable(Screen.Detail().route,
                arguments = listOf(
                    navArgument(Screen.Detail.IMAGE_ENTRY_ARGUMENT_NAME) {
                        type = ImageEntryParamType()
                    }
                )) {
                bottomNavigationState.targetState = false

                val imageEntry = it.arguments?.getParcelable<ImageEntry>(
                    Screen.Detail.IMAGE_ENTRY_ARGUMENT_NAME
                )
                DetailImageScreen(
                    navController = navController,
                    imageEntry = imageEntry
                )
            }
        }
    }

    @Composable
    fun BottomNavigation(
        navController: NavHostController,
        bottomNavigationState: MutableTransitionState<Boolean>,
        modifier: Modifier
    ) {
        AnimatedVisibility(
            visibleState = bottomNavigationState,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = modifier
                .width(dimensionResource(id = R.dimen.bottom_navigation_width))
                .height(dimensionResource(id = R.dimen.bottom_navigation_height))
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.bottom_navigation_rounded_radius))),
            content = {
                BottomNavigation(
                    backgroundColor = colorResource(
                        id = R.color.bottom_navigation_background_color
                    )
                ) {
                    val bottomNavigationItem = listOf(
                        BottomNavItem.Home(MaterialTheme.colors.primary),
                        BottomNavItem.Search(MaterialTheme.colors.primary),
                        BottomNavItem.Favourites(MaterialTheme.colors.secondary),
                    )

                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    bottomNavigationItem.forEach { bottomNavItem ->
                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    painter = painterResource(id = bottomNavItem.iconResId),
                                    contentDescription = stringResource(id = bottomNavItem.titleResId),
                                    modifier = Modifier
                                        .size(dimensionResource(id = R.dimen.bottom_navigation_icon_size))
                                )
                            },
                            selectedContentColor = bottomNavItem.selectedContentColor,
                            unselectedContentColor = colorResource(id = R.color.bottom_navigation_unselected_content_color),
                            selected = currentDestination?.hierarchy?.any {
                                it.route == bottomNavItem.route
                            } == true,
                            onClick = {
                                navController.navigate(bottomNavItem.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            })
    }
}