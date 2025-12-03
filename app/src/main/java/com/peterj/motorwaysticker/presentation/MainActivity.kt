package com.peterj.motorwaysticker.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.peterj.motorwaysticker.R
import com.peterj.motorwaysticker.presentation.navigation.Navigation
import com.peterj.motorwaysticker.presentation.theme.MotorwayStickerTheme
import com.peterj.motorwaysticker.presentation.theme.topBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MotorwayStickerTheme {
                val navController = rememberNavController()
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry?.destination?.route

                Scaffold(
                    topBar = {
                        TopAppBar(
                            modifier = Modifier.clip(
                                RoundedCornerShape(
                                    bottomStart = 20.dp,
                                    bottomEnd = 20.dp
                                )
                            ),
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = topBarColor,
                            ),
                            title = {
                                Text(stringResource(R.string.app_name))
                            },
                            navigationIcon =
                                {
                                    IconButton(onClick = {
                                        if (currentRoute != "main") {
                                            navController.popBackStack()
                                        } else {
                                            finish()
                                        }
                                    }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_arrow_left),
                                            contentDescription = "Back",
                                        )
                                    }
                                },
                        )
                    },
                    content = { innerPadding ->
                        Navigation(navController, Modifier.padding(innerPadding))
                    }
                )
            }
        }
    }
}