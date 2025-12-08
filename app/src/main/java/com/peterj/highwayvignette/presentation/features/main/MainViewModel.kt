package com.peterj.highwayvignette.presentation.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterj.highwayvignette.domain.model.CountyModel
import com.peterj.highwayvignette.domain.model.HighwayInfo
import com.peterj.highwayvignette.domain.model.HighwayVignetteError
import com.peterj.highwayvignette.domain.model.VignetteDetail
import com.peterj.highwayvignette.domain.model.VehicleInfo
import com.peterj.highwayvignette.domain.model.VignetteType
import com.peterj.highwayvignette.domain.usecase.GetHighwayInfoUseCase
import com.peterj.highwayvignette.domain.usecase.GetVehicleInfoUseCase
import com.peterj.highwayvignette.presentation.common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getHighwayInfoUseCase: GetHighwayInfoUseCase,
    private val getVehicleInfoUseCase: GetVehicleInfoUseCase,
) : ViewModel() {

    private val _vehicleInfoState = MutableStateFlow<UiState<VehicleInfo>>(UiState.Empty)
    val vehicleInfoState: StateFlow<UiState<VehicleInfo>> = _vehicleInfoState

    private val _vignettesState = MutableStateFlow<UiState<List<VignetteDetail>>>(UiState.Empty)
    val vignettesState: StateFlow<UiState<List<VignetteDetail>>> = _vignettesState

    private val _selectedVignette = MutableStateFlow<VignetteDetail?>(null)
    val selectedVignette: StateFlow<VignetteDetail?> = _selectedVignette

    private val _counties = MutableStateFlow<List<CountyModel>>(emptyList())
    val counties: StateFlow<List<CountyModel>> = _counties

    init {
        loadData()
    }

    fun selectVignette(selectVignette: VignetteDetail) {
        _selectedVignette.value = selectVignette
    }

    private fun loadData() = viewModelScope.launch {
        _vehicleInfoState.value = UiState.Loading

        val vehicleInfo = runCatching { getVehicleInfoUseCase.execute() }
            .onFailure { e -> handleError(e, isVehicle = true) }
            .getOrNull() ?: return@launch

        _vehicleInfoState.value = UiState.Success(vehicleInfo)

        _vignettesState.value = UiState.Loading

        val highwayInfo = runCatching { getHighwayInfoUseCase.execute() }
            .onFailure { e -> handleError(e, isVehicle = false) }
            .getOrNull() ?: return@launch

        val uiList = filterVignettes(
            highwayInfo = highwayInfo,
            userVehicleCategory = vehicleInfo.type,
            userVehicleVignetteType = vehicleInfo.vignetteType,
        )

        _vignettesState.value = UiState.Success(uiList)
        _counties.value = getCounties(highwayInfo)
    }

    private fun handleError(e: Throwable, isVehicle: Boolean) {
        val error = when (e) {
            is HttpException -> HighwayVignetteError.ServerError(e.code())
            is IOException -> HighwayVignetteError.NetworkError
            else -> HighwayVignetteError.Unknown(e.message)
        }
        if (isVehicle) {
            _vehicleInfoState.value = UiState.Error(error)
        } else {
            _vignettesState.value = UiState.Error(error)
        }
    }

    private fun filterVignettes(
        highwayInfo: HighwayInfo,
        userVehicleCategory: String,
        userVehicleVignetteType: String
    ): List<VignetteDetail> {
        return highwayInfo.vignettes
            .filter { it.vehicleCategory == userVehicleCategory }
            .map {
                VignetteDetail(
                    vignetteCategory = userVehicleVignetteType,
                    vignetteType = VignetteType.fromString(it.types.first()),
                    cost = it.cost,
                    transactionFee = it.transactionFee,
                )
            }
    }

    private fun getCounties(
        highwayInfo: HighwayInfo,
    ): List<CountyModel> {
        val vignette =  highwayInfo.vignettes
            .firstOrNull { vignette ->
                vignette.types.containsAll(highwayInfo.counties.map { it.id })
            }

        return vignette?.let { highwayInfo.counties } ?: emptyList()
    }
}
