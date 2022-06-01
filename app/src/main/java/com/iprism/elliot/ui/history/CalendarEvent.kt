package com.iprism.elliot.ui.history

sealed class CalendarEvent {
    object OnHistoryLoad : CalendarEvent()
    object OnDateChoose : CalendarEvent()
}