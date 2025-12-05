package com.peterj.highwayvignette.domain.model

sealed class HighwayVignetteError {
    data object NetworkError : HighwayVignetteError()
    data class Unknown(val message: String?) : HighwayVignetteError()
}