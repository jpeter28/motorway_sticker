package com.peterj.highwayvignette.presentation.features.county_chooser

import androidx.lifecycle.ViewModel
import com.peterj.highwayvignette.domain.model.CountyModel
import com.peterj.highwayvignette.domain.model.VignetteDetail
import com.peterj.highwayvignette.domain.usecase.AreCountiesConnectedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class CountyChooserViewModel @Inject constructor(
    private val areCountiesConnectedUseCase: AreCountiesConnectedUseCase,
) : ViewModel() {

    private val _counties = MutableStateFlow<List<CountyModel>>(emptyList())
    val counties: StateFlow<List<CountyModel>> = _counties

    private val _selectedVignette = MutableStateFlow<VignetteDetail?>(null)
    val selectedVignette: StateFlow<VignetteDetail?> = _selectedVignette

    private val _checkedStates = MutableStateFlow<Map<CountyModel, Boolean>>(emptyMap())
    val checkedStates: StateFlow<Map<CountyModel, Boolean>> = _checkedStates

    val totalCost: Int
        get() = _checkedStates.value.values.count { it } * (selectedVignette.value?.cost ?: 0)

    fun init(countiesJson: String, selectedVignetteJson: String?) {
        _counties.value = Json.decodeFromString(countiesJson)
        _selectedVignette.value = selectedVignetteJson?.let { Json.decodeFromString(it) }
        _checkedStates.value = counties.value.associateWith { false }
    }

    fun toggleCounty(county: CountyModel) {
        _checkedStates.value = _checkedStates.value.toMutableMap().also {
            it[county] = !(it[county] ?: false)
        }
    }

    fun getSelectedCounties(): List<CountyModel> =
        _checkedStates.value.filterValues { it }.keys.toList()

    fun areSelectedCountiesConnected(): Boolean {
        return areCountiesConnectedUseCase.execute(getSelectedCounties().map { it.name })
    }
}
