package com.peterj.highwayvignette.presentation.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.peterj.highwayvignette.presentation.theme.greyColor

@Composable
fun HighwayVignetteDivider() {
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        thickness = 1.dp,
        color = greyColor.copy(alpha = 0.5f)
    )
}