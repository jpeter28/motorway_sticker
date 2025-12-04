package com.peterj.motorwaysticker.presentation.features.confirm

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.peterj.motorwaysticker.domain.model.SelectedVignetteInfo
import kotlinx.serialization.json.Json

@Composable
fun ConfirmScreen (
    navController: NavHostController = rememberNavController(),
    viewModel: ConfirmViewModel = hiltViewModel()
){

    LaunchedEffect(Unit) {
        val json = navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<String>("selectedCounties")

        val info: SelectedVignetteInfo? = json?.let {
            Json.decodeFromString<SelectedVignetteInfo>(it)
        }
        viewModel.selectedVignetteInfo = info
    }

    if (viewModel.selectedVignetteInfo != null) {
        Text( viewModel.selectedVignetteInfo.toString())
    }
}