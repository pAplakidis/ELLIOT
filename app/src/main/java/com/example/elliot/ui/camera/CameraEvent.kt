package com.example.elliot.ui.camera

import com.example.elliot.domain.model.Food

sealed class CameraEvent {
    object OnCameraButtonClick: CameraEvent()
    object OnBackButtonClick: CameraEvent()
    data class OnDialogYesClick(val food: Food): CameraEvent()
    data class OnDialogOkClick(val food: Food): CameraEvent()
}