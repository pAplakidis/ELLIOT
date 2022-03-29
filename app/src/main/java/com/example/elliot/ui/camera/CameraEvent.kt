package com.example.elliot.ui.camera

sealed class CameraEvent {
    object OnCameraButtonClick : CameraEvent()
    object OnMealEntryDialogOkClick : CameraEvent()
    object OnConfirmationDialogOkClick : CameraEvent()
    object OnPredictionCheckDialogYesClick : CameraEvent()
}