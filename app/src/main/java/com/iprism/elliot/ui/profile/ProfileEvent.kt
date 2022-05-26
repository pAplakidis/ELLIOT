package com.iprism.elliot.ui.profile

sealed class ProfileEvent {
    data class SetMealTime(val meal: String, val timeStart: String, val timeEnd: String) : ProfileEvent()
    object LoadSuggestions : ProfileEvent()
}