package com.peterj.highwayvignette.presentation.common.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun HighwayVignetteSnackbarHost(
    hostState: SnackbarHostState,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
) {
    SnackbarHost(
        hostState = hostState,
        snackbar = { data ->
            Snackbar(containerColor = containerColor) {
                Text(
                    text = data.visuals.message,
                    style = textStyle,
                    color = textColor
                )
            }
        })
}
