package com.peterj.highwayvignette.data.network.request

import com.peterj.highwayvignette.domain.model.HighwayOrder
import kotlinx.serialization.Serializable

@Serializable
data class HighwayOrderRequest(
    val highwayOrders: List<HighwayOrderItem>
)

@Serializable
data class HighwayOrderItem(
    val type: String,
    val category: String,
    val cost: Int
)

fun List<HighwayOrder>.toRequest(): HighwayOrderRequest {
    return HighwayOrderRequest(
        highwayOrders = this.map { it.toData() }
    )
}

fun HighwayOrder.toData(): HighwayOrderItem {
    return HighwayOrderItem(
        type = this.type,
        category = this.category,
        cost = this.cost
    )
}