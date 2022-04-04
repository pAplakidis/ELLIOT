package com.example.elliot.ui.statistics

sealed class StatisticsEvent {
    data class OnDateChoose(val date: String): StatisticsEvent()
    data class OnStatLoad(val mealName: String) : StatisticsEvent()
}