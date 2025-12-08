package com.peterj.highwayvignette.presentation.features.main

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.peterj.highwayvignette.R
import com.peterj.highwayvignette.domain.model.VignetteDetail
import com.peterj.highwayvignette.domain.model.VehicleInfo
import com.peterj.highwayvignette.domain.model.VignetteType
import com.peterj.highwayvignette.presentation.common.components.HighwayVignetteTopAppBar
import com.peterj.highwayvignette.presentation.common.components.UiStateWrapper
import com.peterj.highwayvignette.presentation.common.state.UiState
import com.peterj.highwayvignette.presentation.navigation.Route
import kotlinx.serialization.json.Json

@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val vehicleState by viewModel.vehicleInfoState.collectAsState()
    val vignettesState by viewModel.vignettesState.collectAsState()
    val selectedVignette by viewModel.selectedVignette.collectAsState()

    Scaffold(
        topBar = {
            HighwayVignetteTopAppBar(
                onButtonClick = {
                    activity?.finish()
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                UserInfoCard(
                    uiState = vehicleState,
                    imageRes = R.drawable.ic_car,
                )

                CountryVignetteCard(
                    uiState = vignettesState,
                    selectedVignette = selectedVignette,
                    title = stringResource(R.string.national_vignette),
                    buttonText = stringResource(R.string.purchase),
                    onButtonClick = {
                        if (selectedVignette != null) {
                            val vehicleInfo =
                                (vehicleState as? UiState.Success<VehicleInfo>)?.data
                            if (vehicleInfo != null) {
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    "vehicleInfo", Json.encodeToString(vehicleInfo)
                                )
                            }
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "selectedVignette",
                                Json.encodeToString(selectedVignette)
                            )
                            navController.navigate(Route.ConfirmOrder.route)
                        }
                    },
                    onSelect = { selectedVignette ->
                        viewModel.selectVignette(selectedVignette)
                    }
                )

                val yearVignette = (vignettesState as? UiState.Success)
                    ?.data
                    ?.firstOrNull { it.vignetteType == VignetteType.YEAR }

                if (yearVignette != null) {
                    YearlyVignetteCard(
                        title = stringResource(R.string.yearly_vignettes),
                        onClick = {
                            val vehicleInfo =
                                (vehicleState as? UiState.Success<VehicleInfo>)?.data
                            if (vehicleInfo != null) {
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    "vehicleInfo", Json.encodeToString(vehicleInfo)
                                )
                            }

                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "selectedVignette",
                                Json.encodeToString(yearVignette)
                            )

                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "counties", Json.encodeToString(viewModel.counties.value)
                            )
                            navController.navigate(Route.CountyChooser.route)
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun BaseCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 20.dp, end = 20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
fun UserInfoCard(
    uiState: UiState<VehicleInfo>,
    modifier: Modifier = Modifier,
    imageRes: Int,
) {
    BaseCard(modifier = modifier) {
        UiStateWrapper(state = uiState) { vehicle ->
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = vehicle.plate,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = vehicle.ownerName,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
        }
    }
}

@Composable
fun CountryVignetteCard(
    uiState: UiState<List<VignetteDetail>>,
    selectedVignette: VignetteDetail?,
    modifier: Modifier = Modifier,
    title: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    onSelect: (VignetteDetail) -> Unit,
) {
    BaseCard(modifier = modifier) {
        UiStateWrapper(state = uiState) { vignettes ->
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column(Modifier.selectableGroup()) {
                vignettes
                    .filter { vignette ->
                        vignette.vignetteType != VignetteType.YEAR && vignette.vignetteType != VignetteType.UNKNOWN
                    }
                    .forEach { vignette ->
                        RadioButtonListItemCard(
                            middleText = "${vignette.vignetteCategory} - ${stringResource(vignette.vignetteType.resId)}",
                            rightText = stringResource(R.string.formatted_price, vignette.cost),
                            selected = vignette.vignetteType == selectedVignette?.vignetteType,
                            onSelect = { onSelect(vignette) }
                        )
                    }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onButtonClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(text = buttonText, style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
fun RadioButtonListItemCard(
    middleText: String,
    rightText: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onSelect,
                role = Role.RadioButton,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )
            .padding(vertical = 4.dp)
            .semantics {
                contentDescription = "$middleText, $rightText"
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            2.dp,
            if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selected,
                onClick = null,
            )
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                text = middleText,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = rightText,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun YearlyVignetteCard(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit
) {
    BaseCard(
        modifier = modifier
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
