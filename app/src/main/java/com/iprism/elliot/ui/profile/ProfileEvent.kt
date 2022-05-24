package com.iprism.elliot.ui.profile

sealed class ProfileEvent {
    object LoadSuggestions : ProfileEvent()
}