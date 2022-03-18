package com.example.elliot.ui.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elliot.data.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class CameraViewModel @Inject constructor(
    @Named("FoodRepImpl") private val repository: FoodRepository
) : ViewModel() {

    private var foodId: Int = 0

    val ingredients: MutableList<String> = ArrayList()
    var foodName = String()

    fun onEvent(event: CameraEvent) {
        when (event) {
            is CameraEvent.OnCameraButtonClick -> TODO()
            is CameraEvent.OnBackButtonClick -> TODO()
            is CameraEvent.OnDialogYesClick -> {
                viewModelScope.launch {
                    foodId = repository.getLatestFoodId()
                    event.foodModel.foodId = foodId + 1
                    repository.insertFood(event.foodModel)
                }
            }
            is CameraEvent.OnDialogOkClick -> TODO()
        }
    }
}