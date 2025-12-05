package com.peterj.highwayvignette.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.peterj.highwayvignette.presentation.navigation.Navigation
import com.peterj.highwayvignette.presentation.theme.HighwayVignetteTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HighwayVignetteTheme {
                val navController = rememberNavController()
                Navigation(navController)
            }
        }
    }
}