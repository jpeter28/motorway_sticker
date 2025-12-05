package com.peterj.highwayvignette.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class VehicleInfo(
    val registrationCode: String,
    val type: String,
    val ownerName: String,
    val plate: String,
    val country: Country,
    val vignetteType: String
)

@Serializable
data class Country(
    val hu: String,
    val en: String
)