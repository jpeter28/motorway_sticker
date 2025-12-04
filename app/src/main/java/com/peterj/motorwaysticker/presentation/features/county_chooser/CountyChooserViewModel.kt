package com.peterj.motorwaysticker.presentation.features.county_chooser

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.peterj.motorwaysticker.domain.model.CountyModel
import com.peterj.motorwaysticker.domain.model.SelectedVignetteInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CountyChooserViewModel @Inject constructor() : ViewModel() {
    var selectedVignetteInfo by mutableStateOf<SelectedVignetteInfo?>(null)

    private var _checkedStates = mutableStateMapOf<CountyModel, Boolean>()
    var checkedStates = _checkedStates

    val totalCost: Int
        get() = _checkedStates.values.count { it } * (selectedVignetteInfo?.cost ?: 0)

    fun toggleCounty(county: CountyModel) {
        val current = _checkedStates[county] ?: false
        _checkedStates[county] = !current
    }

    fun getSelectedCounties(): List<CountyModel> =
        _checkedStates.filterValues { it }.keys.toList()
}
