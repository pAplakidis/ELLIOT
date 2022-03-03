package com.example.elliot.util

sealed class UiEvent {
    data class ShowSnackBar(val message: String): UiEvent()
}
