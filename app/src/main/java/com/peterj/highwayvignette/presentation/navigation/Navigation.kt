package com.peterj.highwayvignette.presentation.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.peterj.highwayvignette.presentation.features.confirm_order.ConfirmScreen
import com.peterj.highwayvignette.presentation.features.main.MainScreen
import com.peterj.highwayvignette.presentation.features.success.SuccessScreen
import com.peterj.highwayvignette.presentation.features.county_chooser.CountyChooserScreen

@Composable
fun Navigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController,
        startDestination = Route.Main.route,
        modifier = modifier,
        enterTransition = { slideInHorizontally { it } },
        exitTransition = { slideOutHorizontally { -it } },
        popEnterTransition = { slideInHorizontally { -it } },
        popExitTransition = { slideOutHorizontally { it } }
    ) {
        composable(Route.Main.route) {
            MainScreen(navController)
        }
        composable(Route.CountyChooser.route) {
            CountyChooserScreen(navController)
        }
        composable(Route.ConfirmOrder.route) {
            ConfirmScreen(navController)
        }
        composable(Route.Success.route) {
            SuccessScreen(navController)
        }
    }
}