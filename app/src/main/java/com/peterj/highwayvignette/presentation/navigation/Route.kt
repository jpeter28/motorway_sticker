package com.peterj.highwayvignette.presentation.navigation

sealed class Route(val route: String) {
    data object Main : Route("main")
    data object CountyChooser : Route("county_chooser")
    data object ConfirmOrder : Route("confirm_order")
    data object Success : Route("success")
}