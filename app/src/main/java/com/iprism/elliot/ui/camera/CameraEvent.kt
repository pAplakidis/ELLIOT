package com.iprism.elliot.ui.camera

sealed class CameraEvent {
    data class OnCameraButtonClick(
        val foodName: String,
        val date: String,
        val hour: String,
        val minutes: String
    ) : CameraEvent()
    object OnMealEntryDialogOkClick : CameraEvent()
    object OnConfirmationDialogOkClick : CameraEvent()
    object OnPredictionCheckDialogYesClick : CameraEvent()
}