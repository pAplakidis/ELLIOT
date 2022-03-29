package com.example.elliot.domain.model

data class HistoryModel(
    val food_name: String,
    val time: String? = null,
    val date: String? = null,
    val meal: String? = null
)