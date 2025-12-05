package com.peterj.highwayvignette.presentation.features.confirm_order

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterj.highwayvignette.domain.model.CountyModel
import com.peterj.highwayvignette.domain.model.HighwayOrder
import com.peterj.highwayvignette.domain.model.HighwayOrderResult
import com.peterj.highwayvignette.domain.model.HighwayVignetteError
import com.peterj.highwayvignette.domain.model.VignetteDetail
import com.peterj.highwayvignette.domain.model.VehicleInfo
import com.peterj.highwayvignette.domain.model.VignetteType
import com.peterj.highwayvignette.domain.usecase.PostHighwayOrderUseCase
import com.peterj.highwayvignette.presentation.common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmOrderViewModel @Inject constructor(
    private val postHighwayOrderUseCase: PostHighwayOrderUseCase,
) : ViewModel() {
    var counties by mutableStateOf<List<CountyModel>?>(null)
    var vehicleInfo by mutableStateOf<VehicleInfo?>(null)
    var selectedVignette by mutableStateOf<VignetteDetail?>(null)

    val totalCost: Int
        get() = (counties?.count() ?: 1) * (selectedVignette?.cost
            ?: 0) + (selectedVignette?.transactionFee ?: 0)

    private val _orderState = MutableStateFlow<UiState<HighwayOrderResult>>(UiState.Empty)
    val orderState: StateFlow<UiState<HighwayOrderResult>> = _orderState

    fun orderVignette() {
        val vignetteInfo = selectedVignette ?: return
        val selectedCounties = counties

        viewModelScope.launch {
            _orderState.value = UiState.Loading
            try {
                if (vignetteInfo.vignetteType == VignetteType.YEAR) {
                    if (selectedCounties.isNullOrEmpty()) {
                        return@launch
                    }
                    val orderResult =postHighwayOrderUseCase.execute(
                        selectedCounties.map {
                            HighwayOrder(
                                type = it.id,
                                category = vehicleInfo?.type ?: "",
                                cost = vignetteInfo.cost,
                            )
                        }
                    )
                    _orderState.value = UiState.Success(orderResult)
                } else {
                    val orderResult = postHighwayOrderUseCase.execute(
                        listOf( HighwayOrder(
                            type = vignetteInfo.vignetteType.name,
                            category = vehicleInfo?.type ?: "",
                            cost = vignetteInfo.cost,
                        ))
                    )
                    _orderState.value = UiState.Success(orderResult)
                }
            } catch (e: Exception) {
                _orderState.value = UiState.Error(HighwayVignetteError.Unknown(e.message))
            }
        }
    }
}
