package com.example.elliot.ui.history

sealed class CalendarEvent {
    data class OnDateClick(val datePicked: String) : CalendarEvent()
//    data class OnCardClick(val height: Int) : CalendarEvent()
}