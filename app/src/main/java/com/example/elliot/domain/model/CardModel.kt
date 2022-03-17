package com.example.elliot.domain.model

data class CardModel(
    var foodId: Int? = null,
    var foodIngredients: MutableList<String?> = mutableListOf(),
    val foodDate: String,
    val foodName: String,
    ) {
    fun toCardModel() {
        TODO("Make CardModel in DB")
    }
}