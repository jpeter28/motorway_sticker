package com.peterj.motorwaysticker.presentation.features.confirm_order

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterj.motorwaysticker.domain.model.HighwayOrder
import com.peterj.motorwaysticker.domain.model.HighwayOrderResult
import com.peterj.motorwaysticker.domain.model.SelectVignette
import com.peterj.motorwaysticker.domain.model.SelectedVignetteInfo
import com.peterj.motorwaysticker.domain.model.VehicleInfo
import com.peterj.motorwaysticker.domain.usecase.PostHighwayOrderUseCase
import com.peterj.motorwaysticker.presentation.common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmOrderViewModel @Inject constructor(
    private val postHighwayOrderUseCase: PostHighwayOrderUseCase,
) : ViewModel() {
    var selectedVignetteInfo by mutableStateOf<SelectedVignetteInfo?>(null)
    var vehicleInfo by mutableStateOf<VehicleInfo?>(null)
    var selectedVignette by mutableStateOf<SelectVignette?>(null)

    val totalCost: Int
        get() = (selectedVignetteInfo?.counties?.count() ?: 0) * (selectedVignetteInfo?.cost
            ?: 0) + (selectedVignetteInfo?.transactionFee ?: 0)

    private val _orderState = MutableStateFlow<UiState<HighwayOrderResult>>(UiState.Empty)
    val orderState: StateFlow<UiState<HighwayOrderResult>> = _orderState

    fun orderSticker() {
        val vignetteInfo = selectedVignetteInfo ?: return
        if (vignetteInfo.counties.isEmpty()) {
            _orderState.value = UiState.Error("No counties selected")
            return
        }
        viewModelScope.launch {
            _orderState.value = UiState.Loading
            try {
                val orderResult = postHighwayOrderUseCase.execute(
                    vignetteInfo.counties.map {
                        HighwayOrder(
                            type = it.id,
                            category = vehicleInfo?.type ?: "",
                            cost = vignetteInfo.cost,
                        )
                    }
                )
                _orderState.value = UiState.Success(orderResult)
            } catch (e: Exception) {
                _orderState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
