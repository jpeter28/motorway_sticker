package com.peterj.motorwaysticker.presentation.common.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.peterj.motorwaysticker.R
import com.peterj.motorwaysticker.presentation.theme.topBarColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HighwayStickerTopAppBar(
    onButtonClick: () -> Unit
) {
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
        navigationIcon = {
            IconButton(onClick = onButtonClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = "Back",
                )
            }
        }
    )
}