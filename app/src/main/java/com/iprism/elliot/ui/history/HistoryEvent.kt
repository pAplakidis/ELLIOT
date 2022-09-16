package com.iprism.elliot.ui.history

sealed class HistoryEvent {
    object OnHistoryLoad : HistoryEvent()
    object OnDateChoose : HistoryEvent()
}