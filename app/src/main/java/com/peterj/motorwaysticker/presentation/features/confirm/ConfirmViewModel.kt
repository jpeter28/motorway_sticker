package com.peterj.motorwaysticker.presentation.features.confirm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.peterj.motorwaysticker.domain.model.SelectedVignetteInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConfirmViewModel @Inject constructor() : ViewModel() {
    var selectedVignetteInfo by mutableStateOf<SelectedVignetteInfo?>(null)
}
