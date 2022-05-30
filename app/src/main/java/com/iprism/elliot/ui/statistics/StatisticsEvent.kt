package com.iprism.elliot.ui.statistics

sealed class StatisticsEvent {
    object OnDateChoose : StatisticsEvent()
    data class OnStatLoad(val mealName: String) : StatisticsEvent()
}