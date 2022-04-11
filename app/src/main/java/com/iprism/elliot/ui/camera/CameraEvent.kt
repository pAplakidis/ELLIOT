package com.iprism.elliot.ui.camera

sealed class CameraEvent {
    data class OnCameraButtonClick(val foodName: String) : CameraEvent()
    object OnMealEntryDialogOkClick : CameraEvent()
    object OnConfirmationDialogOkClick : CameraEvent()
    object OnPredictionCheckDialogYesClick : CameraEvent()
}