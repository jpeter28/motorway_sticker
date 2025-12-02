package com.peterj.motorwaysticker.data.network.response

import com.peterj.motorwaysticker.domain.model.CountyModel
import com.peterj.motorwaysticker.domain.model.HighwayInfo
import com.peterj.motorwaysticker.domain.model.VehicleCategory
import com.peterj.motorwaysticker.domain.model.VehicleName
import com.peterj.motorwaysticker.domain.model.Vignette
import kotlinx.serialization.Serializable

@Serializable
data class HighwayInfoResponse(
    val requestId: Long,
    val statusCode: String,
    val payload: PayloadResponse,
    val dataType: String
)

@Serializable
data class PayloadResponse(
    val highwayVignettes: List<HighwayVignetteResponse>,
    val vehicleCategories: List<VehicleCategoryResponse>,
    val counties: List<CountyResponse>
)

@Serializable
data class HighwayVignetteResponse(
    val vignetteType: List<String>,
    val vehicleCategory: String,
    val cost: Int,
    val trxFee: Int,
    val sum: Int
)

@Serializable
data class VehicleCategoryResponse(
    val category: String,
    val vignetteCategory: String,
    val name: NameResponse
)

@Serializable
data class NameResponse(
    val hu: String,
    val en: String
)

@Serializable
data class CountyResponse(
    val id: String,
    val name: String
)

fun HighwayInfoResponse.toDomain(): HighwayInfo {
    return HighwayInfo(
        vignettes = payload.highwayVignettes.map {
            Vignette(
                types = it.vignetteType,
                vehicleCategory = it.vehicleCategory,
                cost = it.cost,
                transactionFee = it.trxFee,
                total = it.sum
            )
        },
        vehicleCategories = payload.vehicleCategories.map {
            VehicleCategory(
                category = it.category,
                vignetteCategory = it.vignetteCategory,
                name = VehicleName(
                    en = it.name.en,
                    hu = it.name.hu,
                ),
            )
        },
        counties = payload.counties.map {
            CountyModel(
                id = it.id,
                name = it.name
            )
        }
    )
}