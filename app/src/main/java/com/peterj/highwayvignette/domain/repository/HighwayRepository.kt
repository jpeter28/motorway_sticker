package com.peterj.highwayvignette.domain.repository

import com.peterj.highwayvignette.domain.model.HighwayInfo
import com.peterj.highwayvignette.domain.model.HighwayOrder
import com.peterj.highwayvignette.domain.model.HighwayOrderResult
import com.peterj.highwayvignette.domain.model.VehicleInfo

interface HighwayRepository {
    suspend fun getHighwayInfo(): HighwayInfo
    suspend fun getVehicleInfo(): VehicleInfo
    suspend fun postOrder(orderRequest: List<HighwayOrder>): HighwayOrderResult
}
