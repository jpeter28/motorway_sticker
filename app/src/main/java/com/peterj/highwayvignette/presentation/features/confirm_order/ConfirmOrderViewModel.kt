package com.peterj.highwayvignette.presentation.features.confirm_order

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
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ConfirmOrderViewModel @Inject constructor(
    private val postHighwayOrderUseCase: PostHighwayOrderUseCase,
) : ViewModel() {

    private val _counties = MutableStateFlow<List<CountyModel>>(emptyList())
    val counties: StateFlow<List<CountyModel>> = _counties

    private val _vehicleInfo = MutableStateFlow<VehicleInfo?>(null)
    val vehicleInfo: StateFlow<VehicleInfo?> = _vehicleInfo

    private val _selectedVignette = MutableStateFlow<VignetteDetail?>(null)
    val selectedVignette: StateFlow<VignetteDetail?> = _selectedVignette

    private val _orderState = MutableStateFlow<UiState<HighwayOrderResult>>(UiState.Empty)
    val orderState: StateFlow<UiState<HighwayOrderResult>> = _orderState

    val totalCost: Int
        get() {
            val vignette = _selectedVignette.value ?: return 0
            val count = if (vignette.vignetteType == VignetteType.YEAR)
                _counties.value.size
            else 1
            return count * vignette.cost + vignette.transactionFee
        }

    fun init(
        countiesJson: String?,
        vehicleJson: String?,
        vignetteJson: String?
    ) {
        _counties.value = countiesJson?.let {
            Json.decodeFromString<List<CountyModel>>(it)
        } ?: emptyList()

        _vehicleInfo.value = vehicleJson?.let {
            Json.decodeFromString<VehicleInfo>(it)
        }

        _selectedVignette.value = vignetteJson?.let {
            Json.decodeFromString<VignetteDetail>(it)
        }
    }

    fun orderVignette() {
        val vignetteInfo = selectedVignette.value ?: return

        viewModelScope.launch {
            _orderState.value = UiState.Loading

            val orders = if (vignetteInfo.vignetteType == VignetteType.YEAR) {
                counties.value.map {
                    HighwayOrder(
                        type = it.id,
                        category = vehicleInfo.value?.type ?: "",
                        cost = vignetteInfo.cost
                    )
                }
            } else {
                listOf(
                    HighwayOrder(
                        type = vignetteInfo.vignetteType.name,
                        category = vehicleInfo.value?.type ?: "",
                        cost = vignetteInfo.cost
                    )
                )
            }

            try {
                val result = postHighwayOrderUseCase.execute(orders)
                _orderState.value = UiState.Success(result)
            } catch (e: HttpException) {
                _orderState.value = UiState.Error(HighwayVignetteError.ServerError(e.code()))
            } catch (e: IOException) {
                _orderState.value = UiState.Error(HighwayVignetteError.NetworkError)
            } catch (e: Exception) {
                _orderState.value = UiState.Error(HighwayVignetteError.Unknown(e.message))
            }
        }
    }
}
