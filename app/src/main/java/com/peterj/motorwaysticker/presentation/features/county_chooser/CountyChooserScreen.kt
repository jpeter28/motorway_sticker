package com.peterj.motorwaysticker.presentation.features.county_chooser

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.json.Json
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.peterj.motorwaysticker.R
import com.peterj.motorwaysticker.domain.model.CountyModel
import com.peterj.motorwaysticker.domain.model.VignetteDetail
import com.peterj.motorwaysticker.presentation.common.components.HighwayStickerSnackbarHost
import com.peterj.motorwaysticker.presentation.common.components.HighwayStickerTopAppBar
import com.peterj.motorwaysticker.presentation.navigation.Route
import kotlinx.coroutines.launch

@Composable
fun CountyChooserScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: CountyChooserViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        val countiesJson = navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<String>("counties")

        viewModel.counties = countiesJson?.let {
            Json.decodeFromString<List<CountyModel>>(it)
        }

        val selectedVignetteJson = navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<String>("selectedVignette")

        viewModel.selectedVignette = selectedVignetteJson?.let {
            Json.decodeFromString<VignetteDetail>(it)
        }
    }

    val counties = viewModel.counties
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = {
            HighwayStickerSnackbarHost(
                hostState = snackbarHostState,
            )
        },
        topBar = {
            HighwayStickerTopAppBar(
                onButtonClick = {
                    navController.popBackStack()
                }
            )
        },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = stringResource(R.string.yearly_stickers),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .background(Color.LightGray, shape = RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Placeholder")
                    }
                }

                if (counties != null) {
                    items(counties) { county ->
                        CountyRowItem(
                            name = county.name,
                            price = viewModel.selectedVignette?.cost ?: 0,
                            checked = viewModel.checkedStates[county] ?: false,
                            onToggle = { viewModel.toggleCounty(county) }
                        )
                    }
                }

                item {
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        thickness = 1.dp,
                        color = Color.Gray.copy(alpha = 0.5f)
                    )
                }

                item {
                    Text(
                        text = stringResource(R.string.total_amount),
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                item {
                    Text(
                        text = stringResource(R.string.formatted_price, viewModel.totalCost),
                        style = MaterialTheme.typography.displayLarge
                    )
                }

                item {
                    Button(
                        onClick = {
                            if (viewModel.areSelectedCountiesConnected()) {
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    "selectedCounties", Json.encodeToString(
                                        viewModel.getSelectedCounties()
                                    )
                                )
                                val vehicleJson = navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.get<String>("vehicleInfo")

                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    "vehicleInfo", vehicleJson
                                )

                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    "selectedVignette", Json.encodeToString(
                                        viewModel.selectedVignette
                                    )
                                )
                                navController.navigate(Route.ConfirmOrder.route)

                            } else {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Selected counties are not connected!")
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.next),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun CountyRowItem(
    name: String,
    price: Int,
    checked: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { onToggle() },
            colors = CheckboxDefaults.colors(
                checkedColor = Color.LightGray,
                checkmarkColor = Color.Gray,
            )
        )
        Text(
            text = name,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelMedium.copy(
                color = if (checked) Color.LightGray
                else MaterialTheme.colorScheme.primary
            )
        )
        Text(
            text = stringResource(R.string.formatted_price, price),
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
