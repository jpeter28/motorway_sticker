package com.peterj.motorwaysticker.domain.model

import com.peterj.motorwaysticker.R

data class SelectVignette(
    val vignetteCategory: String,
    val vignetteType: VignetteType,
    val cost: Int,
)

enum class VignetteType(val resId: Int) {
    DAY(R.string.vignette_type_display_day),
    WEEK(R.string.vignette_type_display_week),
    MONTH(R.string.vignette_type_display_month),
    YEAR(R.string.vignette_type_display_year),
    UNKNOWN(R.string.vignette_type_display_unknown);
}
