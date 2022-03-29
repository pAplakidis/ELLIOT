package com.example.elliot.domain.model

data class CardModel(
    val timeOfDay: String,
    val foodDetails: List<FoodModel>
)