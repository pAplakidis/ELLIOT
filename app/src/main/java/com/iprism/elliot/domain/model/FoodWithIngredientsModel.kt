package com.iprism.elliot.domain.model

data class FoodWithIngredientsModel(
    val foodName: String,
    val ingredients: List<String>
)