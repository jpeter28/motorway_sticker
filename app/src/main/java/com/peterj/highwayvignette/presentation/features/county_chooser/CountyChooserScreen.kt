package com.peterj.highwayvignette.presentation.features.county_chooser

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import com.peterj.highwayvignette.R
import com.peterj.highwayvignette.domain.model.CountyModel
import com.peterj.highwayvignette.domain.model.VignetteDetail
import com.peterj.highwayvignette.domain.model.countiesList
import com.peterj.highwayvignette.domain.model.countyDrawables
import com.peterj.highwayvignette.presentation.common.components.HighwayVignetteDivider
import com.peterj.highwayvignette.presentation.common.components.HighwayVignetteSnackbarHost
import com.peterj.highwayvignette.presentation.common.components.HighwayVignetteTopAppBar
import com.peterj.highwayvignette.presentation.navigation.Route
import com.peterj.highwayvignette.presentation.theme.greyColor
import com.peterj.highwayvignette.presentation.theme.topBarColor
import kotlinx.coroutines.launch

@Composable
fun CountyChooserScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: CountyChooserViewModel = hiltViewModel()
) {
    val counties = viewModel.counties
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        navController.previousBackStackEntry?.savedStateHandle?.let { state ->
            val jsonCounties = state.get<String>("counties")
            viewModel.counties = jsonCounties?.let {
                Json.decodeFromString<List<CountyModel>>(it)
            }
            val jsonSelectedVignette = state.get<String>("selectedVignette")
            viewModel.selectedVignette = jsonSelectedVignette?.let {
                Json.decodeFromString<VignetteDetail>(it)
            }
        }
    }

    Scaffold(
        snackbarHost = {
            HighwayVignetteSnackbarHost(
                hostState = snackbarHostState,
            )
        },
        topBar = {
            HighwayVignetteTopAppBar(
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
                        text = stringResource(R.string.yearly_vignettes),
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                item {
                    Map(
                        checkedStates = viewModel.checkedStates,
                    )
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
                    HighwayVignetteDivider()
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
                    val notConnectedErrorMessage = stringResource(R.string.counties_not_connected)

                    Button(
                        onClick = {
                            if (viewModel.areSelectedCountiesConnected()) {
                                val vehicleJson = navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.get<String>("vehicleInfo")
                                navController.currentBackStackEntry?.savedStateHandle?.let { state ->
                                    state.set(
                                        "selectedCounties", Json.encodeToString(
                                            viewModel.getSelectedCounties()
                                        )
                                    )
                                    state.set(
                                        "selectedVignette", Json.encodeToString(
                                            viewModel.selectedVignette
                                        )
                                    )
                                    state.set(
                                        "vehicleInfo", vehicleJson
                                    )
                                }

                                navController.navigate(Route.ConfirmOrder.route)
                            } else {
                                scope.launch {
                                    snackbarHostState.showSnackbar(notConnectedErrorMessage)
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
            .selectable(
                selected = checked,
                onClick = onToggle,
                role = Role.Checkbox
            )
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
            style = if (checked)
                MaterialTheme.typography.labelMedium.copy(color = greyColor)
            else
                MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary)
        )
        Text(
            text = stringResource(R.string.formatted_price, price),
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

//TODO proper sizing for counties
@Composable
fun Map(
    checkedStates: Map<CountyModel, Boolean>,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.4f)
    ) {
        countiesList.forEach { county ->
            val countyModel =
                checkedStates.keys.firstOrNull { it.id.equals(county.fileName, ignoreCase = true) }
            val isChecked = countyModel?.let { checkedStates[it] } == true

            County(
                fileResource = countyDrawables[county.fileName] ?: R.drawable.year_0,
                left = county.left,
                top = county.top,
                width = county.width,
                height = county.height,
                isSelected = isChecked,
            )
        }
    }
}

@Composable
fun County(
    fileResource: Int,
    left: Float,
    top: Float,
    width: Float,
    height: Float,
    isSelected: Boolean = false,
) {
    Image(
        painter = painterResource(fileResource),
        contentDescription = null,
        modifier = Modifier
            .absoluteOffset(left.dp, top.dp)
            .size(width.dp, height.dp),
        colorFilter = if (isSelected) ColorFilter.tint(topBarColor) else null
    )
}
