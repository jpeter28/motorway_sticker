package com.peterj.highwayvignette.data.repository

import com.peterj.highwayvignette.data.network.HighwayApiService
import com.peterj.highwayvignette.data.network.request.toRequest
import com.peterj.highwayvignette.data.network.response.toDomain
import com.peterj.highwayvignette.domain.model.HighwayInfo
import com.peterj.highwayvignette.domain.model.HighwayOrder
import com.peterj.highwayvignette.domain.model.HighwayOrderResult
import com.peterj.highwayvignette.domain.model.VehicleInfo
import com.peterj.highwayvignette.domain.repository.HighwayRepository
import toDomain
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HighwayRepositoryImplementation @Inject constructor(
    private val api: HighwayApiService
) : HighwayRepository {
    override suspend fun getHighwayInfo(): HighwayInfo = api.getHighwayInfo().toDomain()

    override suspend fun getVehicleInfo(): VehicleInfo = api.getVehicleInfo().toDomain()

    override suspend fun postOrder(orderRequest: List<HighwayOrder>): HighwayOrderResult = api.postOrder(orderRequest.toRequest()).toDomain()
}