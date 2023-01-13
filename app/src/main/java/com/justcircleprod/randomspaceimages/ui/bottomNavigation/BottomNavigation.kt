package com.justcircleprod.randomspaceimages.ui.bottomNavigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import com.justcircleprod.randomspaceimages.R

@Composable
fun BottomNavigation(navController: NavController, items: List<BottomNavItem>) {
    val bottomNavigationState = remember { MutableTransitionState(true) }

    navController.addOnDestinationChangedListener { _, destination, _ ->
        bottomNavigationState.targetState = when (destination.id) {
            R.id.navigation_random, R.id.navigation_more, R.id.navigation_apod -> true
            else -> false
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    AnimatedVisibility(
        visibleState = bottomNavigationState,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        modifier = Modifier
            .width(dimensionResource(id = R.dimen.bottom_navigation_width))
            .height(dimensionResource(id = R.dimen.bottom_navigation_height))
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.bottom_navigation_rounded_radius))),
        content = {
            androidx.compose.material.BottomNavigation(
                backgroundColor = colorResource(id = R.color.bottom_navigation_background)
            ) {
                items.forEach { bottomNavItem ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = bottomNavItem.iconResId),
                                contentDescription = stringResource(id = bottomNavItem.titleResId),
                                modifier = Modifier
                                    .size(dimensionResource(id = R.dimen.bottom_navigation_icon_size))
                            )
                        },
                        selectedContentColor = colorResource(id = R.color.primary),
                        unselectedContentColor = colorResource(id = R.color.bottom_navigation_unselected),
                        selected = currentDestination?.hierarchy?.any {
                            it.id == bottomNavItem.navigationId
                        } == true,
                        onClick = {
                            navController.navigate(
                                bottomNavItem.actionId,
                                null,
                                navOptions {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                })
                        }
                    )
                }
            }
        })
}