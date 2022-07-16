package com.iprism.elliot.ui.profile

sealed class ProfileEvent {
    data class SetMealTime(val meal: String, val hourStart: Int, val hourEnd: Int) :
        ProfileEvent()

    object LoadSuggestions : ProfileEvent()
}