package com.peterj.highwayvignette.presentation.common.state

import com.peterj.highwayvignette.domain.model.HighwayVignetteError

sealed class UiState<out T> {
    data object Empty : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val error: HighwayVignetteError) : UiState<Nothing>()
}