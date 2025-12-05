package com.peterj.motorwaysticker.presentation.features.confirm_order

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.peterj.motorwaysticker.R
import com.peterj.motorwaysticker.domain.model.CountyModel
import com.peterj.motorwaysticker.domain.model.VignetteDetail
import com.peterj.motorwaysticker.domain.model.VehicleInfo
import com.peterj.motorwaysticker.domain.model.VignetteType
import com.peterj.motorwaysticker.presentation.common.components.HighwayStickerSnackbarHost
import com.peterj.motorwaysticker.presentation.common.components.HighwayStickerTopAppBar
import com.peterj.motorwaysticker.presentation.common.components.UiStateWrapper
import com.peterj.motorwaysticker.presentation.navigation.Route
import kotlinx.serialization.json.Json

@Composable
fun ConfirmScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: ConfirmOrderViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        val jsonSelectedCounties = navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<String>("selectedCounties")

        viewModel.counties = jsonSelectedCounties?.let {
            Json.decodeFromString<List<CountyModel>>(it)
        }

        val vehicleInfoJson: String? = navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<String>("vehicleInfo")

        val vehicleInfo: VehicleInfo? = vehicleInfoJson?.let {
            Json.decodeFromString<VehicleInfo>(it)
        }

        val selectedVignetteJson: String? = navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<String>("selectedVignette")

        val selectedVignette: VignetteDetail? = selectedVignetteJson?.let {
            Json.decodeFromString<VignetteDetail>(it)
        }

        viewModel.vehicleInfo = vehicleInfo
        viewModel.selectedVignette = selectedVignette
    }

    val counties = viewModel.counties
    val orderState by viewModel.orderState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

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
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        text = stringResource(R.string.confirm_order),
                        style = MaterialTheme.typography.titleLarge
                    )
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.plate_number),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = viewModel.vehicleInfo?.plate ?: "",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.sticker_type),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = stringResource(
                                viewModel.selectedVignette?.vignetteType?.resId
                                    ?: R.string.vignette_type_display_unknown
                            ),
                            style = MaterialTheme.typography.bodySmall
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

                if (viewModel.selectedVignette?.vignetteType != VignetteType.YEAR && viewModel.selectedVignette?.vignetteType != VignetteType.UNKNOWN) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(
                                    viewModel.selectedVignette?.vignetteType?.resId
                                        ?: R.string.vignette_type_display_unknown
                                ), style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = stringResource(
                                    R.string.formatted_price,
                                    viewModel.selectedVignette?.cost ?: 0
                                ),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(R.string.transaction_fee),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = stringResource(
                                    R.string.formatted_price,
                                    viewModel.selectedVignette?.transactionFee ?: 0
                                ),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                if (counties != null) {
                    items(counties) { county ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = county.name, style = MaterialTheme.typography.titleSmall)
                            Text(
                                text = stringResource(
                                    R.string.formatted_price,
                                    viewModel.selectedVignette?.cost ?: 0
                                ),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(R.string.transaction_fee),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = stringResource(
                                    R.string.formatted_price,
                                    viewModel.selectedVignette?.transactionFee ?: 0
                                ),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
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
                            viewModel.orderSticker()
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

                item {
                    OutlinedButton(
                        onClick = {
                            navController.popBackStack()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }


            }
            UiStateWrapper(
                state = orderState,
                success = {
                    LaunchedEffect(Unit) {
                        navController.navigate(Route.Success.route)
                    }
                },
                error = { message ->
                    LaunchedEffect(message) {
                        snackbarHostState.showSnackbar(message)
                    }
                }
            )
        }
    )
}