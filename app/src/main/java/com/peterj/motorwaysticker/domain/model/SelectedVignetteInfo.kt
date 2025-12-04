package com.peterj.motorwaysticker.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SelectedVignetteInfo(
    val countyNames: List<String>,
    val cost: Int,
    val transactionFee: Int
)
