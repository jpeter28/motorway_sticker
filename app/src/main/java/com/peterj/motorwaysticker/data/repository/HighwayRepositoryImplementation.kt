package com.peterj.motorwaysticker.data.repository

import com.peterj.motorwaysticker.data.network.HighwayApiService
import com.peterj.motorwaysticker.data.network.request.toRequest
import com.peterj.motorwaysticker.data.network.response.toDomain
import com.peterj.motorwaysticker.domain.model.HighwayInfo
import com.peterj.motorwaysticker.domain.model.HighwayOrder
import com.peterj.motorwaysticker.domain.model.HighwayOrderResult
import com.peterj.motorwaysticker.domain.model.VehicleInfo
import com.peterj.motorwaysticker.domain.repository.HighwayRepository
import toDomain
import javax.inject.Inject

class HighwayRepositoryImplementation @Inject constructor(
    private val api: HighwayApiService
) : HighwayRepository {
    override suspend fun getHighwayInfo(): HighwayInfo = api.getHighwayInfo().toDomain()

    override suspend fun getVehicleInfo(): VehicleInfo = api.getVehicleInfo().toDomain()

    override suspend fun postOrder(orderRequest: List<HighwayOrder>): HighwayOrderResult = api.postOrder(orderRequest.toRequest()).toDomain()
}