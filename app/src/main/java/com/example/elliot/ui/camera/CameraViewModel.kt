package com.example.elliot.ui.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elliot.data.repository.FoodRepository
import com.example.elliot.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val repository: FoodRepository
) : ViewModel() {

    fun onEvent(event: CameraEvent) {
        when (event) {
            is CameraEvent.OnCameraButtonClick -> TODO()
            is CameraEvent.OnBackButtonClick -> {
//                viewModelScope.launch {
//                    repository.getFoods().onEach {
//                        _uiEvent.emit(UiEvent.ShowSnackBar(
//                            it[0].foodName
//                        ))
//                    }
//                }
            }
            is CameraEvent.OnDialogYesClick -> {
                viewModelScope.launch {
                    repository.insertFood(event.food)
                }
            }
            is CameraEvent.OnDialogOkClick -> TODO()
        }
    }
}