package com.peterj.motorwaysticker.domain.repository

import com.peterj.motorwaysticker.domain.model.HighwayInfo
import com.peterj.motorwaysticker.domain.model.HighwayOrder
import com.peterj.motorwaysticker.domain.model.HighwayOrderResult
import com.peterj.motorwaysticker.domain.model.VehicleInfo

interface HighwayRepository {
    suspend fun getHighwayInfo(): HighwayInfo
    suspend fun getVehicleInfo(): VehicleInfo
    suspend fun postOrder(orderRequest: List<HighwayOrder>): HighwayOrderResult
}
