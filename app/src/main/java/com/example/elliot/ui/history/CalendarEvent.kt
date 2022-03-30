package com.example.elliot.ui.history

sealed class CalendarEvent {
    data class OnHistoryLoad(val foodName: String) : CalendarEvent()
}