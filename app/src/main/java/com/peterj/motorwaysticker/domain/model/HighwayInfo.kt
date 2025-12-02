package com.peterj.motorwaysticker.domain.model

data class HighwayInfo(
    val vignettes: List<Vignette>,
    val vehicleCategories: List<VehicleCategory>,
    val counties: List<CountyModel>
)

data class Vignette(
    val types: List<String>,
    val vehicleCategory: String,
    val cost: Int,
    val transactionFee: Int,
    val total: Int
)

data class VehicleCategory(
    val category: String,
    val vignetteCategory: String,
    val name: VehicleName,
)

data class VehicleName(
    val hu: String,
    val en: String
)

data class CountyModel(
    val id: String,
    val name: String
)