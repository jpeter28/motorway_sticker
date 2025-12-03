package com.peterj.motorwaysticker.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.peterj.motorwaysticker.presentation.features.confirm.ConfirmScreen
import com.peterj.motorwaysticker.presentation.features.main.MainScreen
import com.peterj.motorwaysticker.presentation.features.success.SuccessScreen
import com.peterj.motorwaysticker.presentation.features.yearly_stickers.YearlyStickersScreen

@Composable
fun Navigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController, startDestination = "main", modifier = modifier) {
        composable("main") {
            MainScreen(navController)
        }

        composable(
            "yearly_stickers",
        ) {
            YearlyStickersScreen()
        }

        composable(
            "confirm",
        ) {
            ConfirmScreen()
        }

        composable(
            "success",
        ) {
            SuccessScreen()
        }
    }
}