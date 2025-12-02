package com.peterj.motorwaysticker.domain.model

data class VehicleInfo(
    val registrationCode: String,
    val type: String,
    val ownerName: String,
    val plate: String,
    val country: Country,
    val vignetteType: String
)

data class Country(
    val hu: String,
    val en: String
)