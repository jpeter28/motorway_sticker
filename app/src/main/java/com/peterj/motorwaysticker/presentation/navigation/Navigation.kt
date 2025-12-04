package com.peterj.motorwaysticker.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.peterj.motorwaysticker.presentation.features.confirm.ConfirmScreen
import com.peterj.motorwaysticker.presentation.features.main.MainScreen
import com.peterj.motorwaysticker.presentation.features.success.SuccessScreen
import com.peterj.motorwaysticker.presentation.features.county_chooser.CountyChooserScreen

@Composable
fun Navigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController, startDestination = "main", modifier = modifier) {
        composable("main") {
            MainScreen(navController)
        }

        composable(
            "county_chooser",
        ) {
            CountyChooserScreen(navController)
        }

        composable(
            "confirm",
        ) {
            ConfirmScreen(navController)
        }

        composable(
            "success",
        ) {
            SuccessScreen()
        }
    }
}