package com.peterj.motorwaysticker.domain.usecase

import com.peterj.motorwaysticker.core.di.IoDispatcher
import com.peterj.motorwaysticker.domain.model.VehicleInfo
import com.peterj.motorwaysticker.domain.repository.HighwayRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetVehicleInfoUseCase @Inject constructor(
    private val repository: HighwayRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun execute(): VehicleInfo =
        withContext(ioDispatcher) {
            repository.getVehicleInfo()
        }
}