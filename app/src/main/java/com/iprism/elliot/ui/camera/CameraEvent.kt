package com.iprism.elliot.ui.camera

sealed class CameraEvent {
    object OnCameraButtonClick : CameraEvent()
    data class OnPredictionCheckDialogYesClick(val foodName: String) : CameraEvent()
    object OnMealEntryDialogOkClick : CameraEvent()
    data class OnConfirmationDialogOkClick(val newFoodName: String) : CameraEvent()
}