package com.example.elliot.ui.camera

import com.example.elliot.domain.model.FoodModel

sealed class CameraEvent {
    object OnCameraButtonClick: CameraEvent()
    object OnBackButtonClick: CameraEvent()
    data class OnDialogYesClick(val foodModel: FoodModel): CameraEvent()
    data class OnDialogOkClick(val foodModel: FoodModel): CameraEvent()
}