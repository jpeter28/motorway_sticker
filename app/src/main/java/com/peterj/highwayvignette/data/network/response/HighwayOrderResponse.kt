package com.peterj.highwayvignette.data.network.response

import com.peterj.highwayvignette.data.network.request.HighwayOrderItem
import com.peterj.highwayvignette.domain.model.HighwayOrder
import com.peterj.highwayvignette.domain.model.HighwayOrderResult
import kotlinx.serialization.Serializable

@Serializable
data class HighwayOrderResponse(
    val statusCode: String,
    val receivedOrders: List<HighwayOrderItem>
)

fun HighwayOrderResponse.toDomain(): HighwayOrderResult {
    return HighwayOrderResult(
        orders = receivedOrders.map { it.toDomain() }
    )
}

fun HighwayOrderItem.toDomain(): HighwayOrder {
    return HighwayOrder(
        type = this.type,
        category = this.category,
        cost = this.cost
    )
}