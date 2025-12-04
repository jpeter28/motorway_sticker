package com.peterj.motorwaysticker.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SelectedVignetteInfo(
    val counties: List<CountyModel>,
    val cost: Int,
    val transactionFee: Int
)
