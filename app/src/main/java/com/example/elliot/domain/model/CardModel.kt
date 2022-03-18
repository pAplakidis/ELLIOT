package com.example.elliot.domain.model

data class CardModel(
    val timeOfDay: String,
    var foodDetails: MutableList<FoodModel> = mutableListOf()
    ) {
    fun toCardModel() {
        TODO("Make CardModel in DB")
    }
}