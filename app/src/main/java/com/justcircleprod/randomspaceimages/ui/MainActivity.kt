package com.justcircleprod.randomspaceimages.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.justcircleprod.randomspaceimages.R
import com.justcircleprod.randomspaceimages.data.dataStore.DataStoreConstants
import com.justcircleprod.randomspaceimages.data.models.ImageEntry
import com.justcircleprod.randomspaceimages.data.models.ImageEntryParamType
import com.justcircleprod.randomspaceimages.ui.bottomNavItem.BottomNavItem
import com.justcircleprod.randomspaceimages.ui.detailImage.DetailImageScreen
import com.justcircleprod.randomspaceimages.ui.detailImage.DetailViewModel
import com.justcircleprod.randomspaceimages.ui.home.HomeScreen
import com.justcircleprod.randomspaceimages.ui.home.favourites.FavouriteImageListViewModel
import com.justcircleprod.randomspaceimages.ui.home.random.RandomImageListViewModel
import com.justcircleprod.randomspaceimages.ui.more.MoreScreen
import com.justcircleprod.randomspaceimages.ui.more.MoreViewModel
import com.justcircleprod.randomspaceimages.ui.navigation.Screen
import com.justcircleprod.randomspaceimages.ui.search.SearchScreen
import com.justcircleprod.randomspaceimages.ui.search.searchResult.SearchResultScreen
import com.justcircleprod.randomspaceimages.ui.search.searchResult.SearchResultViewModel
import com.justcircleprod.randomspaceimages.ui.theme.*
import com.justcircleprod.randomspaceimages.util.parcelable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val viewModel: MainViewModel by viewModels()

    private var isFirstStart = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*MobileAds.initialize(this) { }*/

        val initJob = lifecycleScope.launch {
            setContent {
                val themeValue =
                    viewModel.themeValue.collectAsState(initial = "not_initialized")

                if (themeValue.value != "not_initialized") {
                    isFirstStart = false
                }

                val darkTheme = when (themeValue.value) {
                    DataStoreConstants.LIGHT_THEME -> false
                    DataStoreConstants.DARK_THEME -> true
                    else -> isSystemInDarkTheme()
                }

                val systemUiController = rememberSystemUiController()

                DisposableEffect(systemUiController, !darkTheme) {
                    systemUiController.setStatusBarColor(
                        color = if (!darkTheme) LightBackground else DarkBackground,
                        darkIcons = !darkTheme
                    )
                    systemUiController.setNavigationBarColor(
                        color = if (!darkTheme) LightNavigationBar else DarkNavigationBar,
                        darkIcons = !darkTheme
                    )

                    onDispose {}
                }

                val navController = rememberNavController()
                val bottomNavigationState = remember { MutableTransitionState(true) }

                RandomSpaceImagesTheme(darkTheme = darkTheme) {
                    CustomColorsProvider(darkTheme = darkTheme) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colors.background
                        ) {
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
        }

        installSplashScreen().setKeepOnScreenCondition {
            !(initJob.isCompleted && !isFirstStart)
        }
    }

    @Composable
    fun NavHost(
        navController: NavHostController,
        bottomNavigationState: MutableTransitionState<Boolean>
    ) {
        val randomImageListViewModel: RandomImageListViewModel = hiltViewModel()
        val favouriteImageListViewModel: FavouriteImageListViewModel = hiltViewModel()

        NavHost(
            navController = navController,
            startDestination = navController.currentDestination?.route ?: Screen.Home.route,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(Screen.Home.route) {
                bottomNavigationState.targetState = true

                HomeScreen(
                    navController = navController,
                    randomImageListViewModel = randomImageListViewModel,
                    favouriteImageListViewModel = favouriteImageListViewModel
                )
            }

            composable(Screen.Search.route) {
                bottomNavigationState.targetState = true

                SearchScreen(navController = navController)
            }
            composable(
                Screen.SearchResult().route,
                arguments = listOf(
                    navArgument(Screen.SearchResult.YEAR_START_ARGUMENT_NAME) {
                        type = NavType.IntType
                    },
                    navArgument(Screen.SearchResult.YEAR_END_ARGUMENT_NAME) {
                        type = NavType.IntType
                    },
                    navArgument(Screen.SearchResult.Q_ARGUMENT_NAME) {
                        type = NavType.StringType
                        defaultValue = ""
                    },
                )
            ) {
                bottomNavigationState.targetState = false

                val searchResultViewModel: SearchResultViewModel = hiltViewModel()
                SearchResultScreen(navController = navController, viewModel = searchResultViewModel)
            }

            composable(Screen.Detail().route,
                arguments = listOf(
                    navArgument(Screen.Detail.IMAGE_ENTRY_ARGUMENT_NAME) {
                        type = ImageEntryParamType()
                    }
                )) {
                bottomNavigationState.targetState = false

                val imageEntry = it.arguments?.parcelable<ImageEntry>(
                    Screen.Detail.IMAGE_ENTRY_ARGUMENT_NAME
                )
                val detailViewModel: DetailViewModel = hiltViewModel()

                DetailImageScreen(
                    navController = navController,
                    imageEntry = imageEntry,
                    viewModel = detailViewModel
                )
            }

            composable(Screen.More.route) {
                bottomNavigationState.targetState = true

                val moreViewModel: MoreViewModel = hiltViewModel()

                MoreScreen(viewModel = moreViewModel)
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
                    backgroundColor = MaterialTheme.customColors.bottomNavigationBackground
                ) {
                    val bottomNavigationItem = listOf(
                        BottomNavItem.Home(MaterialTheme.colors.primary),
                        BottomNavItem.Search(MaterialTheme.colors.primary),
                        BottomNavItem.More(MaterialTheme.colors.primary)
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
                            unselectedContentColor = MaterialTheme.customColors.bottomNavigationUnselected,
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