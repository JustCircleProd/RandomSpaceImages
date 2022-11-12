package com.justcircleprod.randomspaceimages.ui.common

import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections

/** Use this function instead of navigate(directions: NavDirections). Helps avoid a crash caused by the user quickly tapping 2 (or more) times on a view that triggers an navigation action **/
fun NavController.navigateSafety(
    @IdRes destinationId: Int,
    direction: NavDirections
) {
    if (this.currentDestination?.id == destinationId) {
        this.navigate(direction)
    }
}