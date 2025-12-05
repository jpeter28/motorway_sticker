package com.peterj.highwayvignette.domain.model

import com.peterj.highwayvignette.R
import kotlinx.serialization.Serializable

@Serializable
data class VignetteDetail(
    val vignetteCategory: String,
    val vignetteType: VignetteType,
    val cost: Int,
    val transactionFee: Int,
)

enum class VignetteType(val resId: Int) {
    DAY(R.string.vignette_type_display_day),
    WEEK(R.string.vignette_type_display_week),
    MONTH(R.string.vignette_type_display_month),
    YEAR(R.string.vignette_type_display_year),
    UNKNOWN(R.string.vignette_type_display_unknown);

    companion object {
        fun fromString(value: String): VignetteType =
            runCatching { valueOf(value.uppercase()) }
                .getOrDefault(UNKNOWN)
    }
}
