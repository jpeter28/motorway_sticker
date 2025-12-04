package com.peterj.motorwaysticker.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.peterj.motorwaysticker.presentation.navigation.Navigation
import com.peterj.motorwaysticker.presentation.theme.MotorwayStickerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MotorwayStickerTheme {
                val navController = rememberNavController()
                Navigation(navController)
            }
        }
    }
}