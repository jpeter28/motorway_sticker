package com.peterj.motorwaysticker.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterj.motorwaysticker.domain.model.HighwayOrder
import com.peterj.motorwaysticker.domain.usecase.GetHighwayInfoUseCase
import com.peterj.motorwaysticker.domain.usecase.GetVehicleInfoUseCase
import com.peterj.motorwaysticker.domain.usecase.PostHighwayOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getHighwayInfoUseCase: GetHighwayInfoUseCase,
    private val getVehicleInfoUseCase: GetVehicleInfoUseCase,
    private val postHighwayOrderUseCase: PostHighwayOrderUseCase,
) : ViewModel() {

    private val _testResult = MutableStateFlow("Loading...")
    val testResult: StateFlow<String> = _testResult

    fun runApiTest() {
        viewModelScope.launch {
            val resultBuilder = StringBuilder()

            try {
                val highwayInfo = getHighwayInfoUseCase.execute()
                resultBuilder.append("Highway info loaded: ${highwayInfo.vignettes.size} vignettes\n")
            } catch (e: Exception) {
                resultBuilder.append("Highway info error: ${e.message}\n")
            }

            try {
                val vehicleInfo = getVehicleInfoUseCase.execute()
                resultBuilder.append("Vehicle: ${vehicleInfo.ownerName}, Plate: ${vehicleInfo.plate}\n")
            } catch (e: Exception) {
                resultBuilder.append("Vehicle info error: ${e.message}\n")
            }

            try {
                val orderResult = postHighwayOrderUseCase.execute(
                    listOf(
                        HighwayOrder(type = "DAY", category = "CAR", cost = 5000)
                    )
                )
                resultBuilder.append("Order received: ${orderResult.orders.size} items\n")
            } catch (e: Exception) {
                resultBuilder.append("Order error: ${e.message}\n")
            }

            _testResult.value = resultBuilder.toString()
        }
    }
}