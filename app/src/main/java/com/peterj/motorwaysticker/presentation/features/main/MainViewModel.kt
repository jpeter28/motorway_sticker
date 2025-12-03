package com.peterj.motorwaysticker.presentation.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterj.motorwaysticker.domain.model.HighwayInfo
import com.peterj.motorwaysticker.presentation.model.UiVignette
import com.peterj.motorwaysticker.domain.model.VehicleInfo
import com.peterj.motorwaysticker.presentation.model.VignetteType
import com.peterj.motorwaysticker.domain.usecase.GetHighwayInfoUseCase
import com.peterj.motorwaysticker.domain.usecase.GetVehicleInfoUseCase
import com.peterj.motorwaysticker.presentation.common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getHighwayInfoUseCase: GetHighwayInfoUseCase,
    private val getVehicleInfoUseCase: GetVehicleInfoUseCase,
) : ViewModel() {

    private val _vehicleInfoState = MutableStateFlow<UiState<VehicleInfo>>(UiState.Empty)
    val vehicleInfoState: StateFlow<UiState<VehicleInfo>> = _vehicleInfoState

    private val _vignettesState = MutableStateFlow<UiState<List<UiVignette>>>(UiState.Empty)
    val vignettesState: StateFlow<UiState<List<UiVignette>>> = _vignettesState


    fun loadData() = viewModelScope.launch {
        _vehicleInfoState.value = UiState.Loading
        _vignettesState.value = UiState.Loading

        val vehicleInfo = try {
            getVehicleInfoUseCase.execute()
        } catch (e: Exception) {
            _vehicleInfoState.value = UiState.Error(e.message ?: "Unknown error")
            _vignettesState.value = UiState.Error(e.message ?: "Unknown error")
            return@launch
        }
        _vehicleInfoState.value = UiState.Success(vehicleInfo)

        val highwayInfo = try {
            getHighwayInfoUseCase.execute()
        } catch (e: Exception) {
            _vignettesState.value = UiState.Error(e.message ?: "Unknown error")
            return@launch
        }

        val uiList = filterVignettes(
            highwayInfo = highwayInfo,
            userVehicleCategory = vehicleInfo.type,
            userVehicleVignetteType = vehicleInfo.vignetteType,
        )

        _vignettesState.value = UiState.Success(uiList)
    }

    private fun filterVignettes(
        highwayInfo: HighwayInfo,
        userVehicleCategory: String,
        userVehicleVignetteType: String
    ): List<UiVignette> {
        return highwayInfo.vignettes
            .filter { it.vehicleCategory == userVehicleCategory }
            .filter { vignette ->
                vignette.types.none { type ->
                    type == "YEAR" || type.startsWith("YEAR_")
                } || vignette.types.isEmpty()
            }
            .map {
                UiVignette(
                    vignetteCategory = userVehicleVignetteType,
                    vignetteType = VignetteType.valueOf(it.types.first()),
                    cost = it.cost,
                )
            }
    }
}
