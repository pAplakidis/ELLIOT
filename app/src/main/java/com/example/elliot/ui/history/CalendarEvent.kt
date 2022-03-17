package com.example.elliot.ui.history

import com.example.elliot.domain.model.CardModel
import com.example.elliot.domain.model.FoodModel

sealed class CalendarEvent {
    data class OnDateClick(val cardModel: CardModel, val datePicked: String) : CalendarEvent()
    data class OnCardClick(val cardModel: CardModel) : CalendarEvent()
}