package com.peterj.motorwaysticker.presentation.features.county_chooser

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.peterj.motorwaysticker.domain.model.SelectedVignetteInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CountyChooserViewModel @Inject constructor() : ViewModel() {
    var selectedVignetteInfo by mutableStateOf<SelectedVignetteInfo?>(null)

    private var _checkedStates = mutableStateMapOf<String, Boolean>()
    var checkedStates = _checkedStates

    val totalCost: Int
        get() = _checkedStates.values.count { it } * (selectedVignetteInfo?.cost ?: 0)

    fun toggleCounty(name: String) {
        val current = _checkedStates[name] ?: false
        _checkedStates[name] = !current
    }

    fun getSelectedCounties(): List<String> =
        _checkedStates.filterValues { it }.keys.toList()
}
