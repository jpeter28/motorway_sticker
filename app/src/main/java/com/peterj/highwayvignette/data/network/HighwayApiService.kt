package com.peterj.highwayvignette.data.network

import VehicleInfoResponse
import com.peterj.highwayvignette.data.network.request.HighwayOrderRequest
import com.peterj.highwayvignette.data.network.response.HighwayInfoResponse
import com.peterj.highwayvignette.data.network.response.HighwayOrderResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface HighwayApiService {
    @GET("v1/highway/info")
    suspend fun getHighwayInfo(): HighwayInfoResponse

    @GET("v1/highway/vehicle")
    suspend fun getVehicleInfo(): VehicleInfoResponse

    @POST("v1/highway/order")
    suspend fun postOrder(@Body highwayOrderRequest: HighwayOrderRequest): HighwayOrderResponse
}