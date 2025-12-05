package com.peterj.highwayvignette.domain.model

import com.peterj.highwayvignette.R

data class CountyMapData(
    val fileName: String,
    val left: Float,
    val top: Float,
    val width: Float,
    val height: Float
)

val countiesList = listOf(
    CountyMapData("year_24", 33.5f, 169.06f, 63.44f, 72.79f),
    CountyMapData("year_27", 0f, 135.74f, 55.48f, 47.21f),
    CountyMapData("year_16", 89.45f, 129.94f, 42.47f, 58.15f),
    CountyMapData("year_28", 44.61f, 134.48f, 54.64f, 48.35f),
    CountyMapData("year_23", 119.68f, 98.74f, 64.98f, 73.08f),
    CountyMapData("year_26", 86.3f, 177.15f, 47.02f, 48.87f),
    CountyMapData("year_12", 67.89f, 205.66f, 58.45f, 44.34f),
    CountyMapData("year_29", 11.03f, 162.58f, 48.85f, 49.27f),
    CountyMapData("year_17", 16.81f, 99.26f, 68.42f, 49.67f),
    CountyMapData("year_21", 82.20f, 114.00f, 49.00f, 31.26f),
    CountyMapData("year_15", 162.69f, 180.48f, 53.16f, 45.31f),
    CountyMapData("year_13", 198.62f, 152.66f, 58.98f, 64.59f),
    CountyMapData("year_0", 130.63f, 128.05f, 18.58f, 16.63f),
    CountyMapData("year_22", 131.09f, 86.47f, 52.74f, 36.77f),
    CountyMapData("year_19", 160.13f, 92.66f, 54.76f, 47.78f),
    CountyMapData("year_18", 217.45f, 104.94f, 59.40f, 66.48f),
    CountyMapData("year_25", 228.62f, 73.68f, 82.38f, 54.38f),
    CountyMapData("year_14", 181.99f, 64.00f, 93.37f, 61.95f),
    CountyMapData("year_11", 120.04f, 159.26f, 66.94f, 79.81f),
    CountyMapData("year_20", 163.87f, 123.16f, 62.66f, 60.76f)
)

val countyDrawables = mapOf(
    "year_11" to R.drawable.year_11,
    "year_12" to R.drawable.year_12,
    "year_13" to R.drawable.year_13,
    "year_14" to R.drawable.year_14,
    "year_15" to R.drawable.year_15,
    "year_16" to R.drawable.year_16,
    "year_17" to R.drawable.year_17,
    "year_18" to R.drawable.year_18,
    "year_19" to R.drawable.year_19,
    "year_20" to R.drawable.year_20,
    "year_21" to R.drawable.year_21,
    "year_22" to R.drawable.year_22,
    "year_23" to R.drawable.year_23,
    "year_24" to R.drawable.year_24,
    "year_25" to R.drawable.year_25,
    "year_26" to R.drawable.year_26,
    "year_27" to R.drawable.year_27,
    "year_28" to R.drawable.year_28,
    "year_29" to R.drawable.year_29,
    "year_0"  to R.drawable.year_0
)