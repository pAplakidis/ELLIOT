package com.iprism.elliot.util

sealed class UiEvent {
    data class ShowSnackBar(val message: String) : UiEvent()
    data class ShowToast(val message: String) : UiEvent()
}