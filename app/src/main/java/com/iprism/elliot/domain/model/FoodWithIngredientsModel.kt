package com.iprism.elliot.domain.model

import com.iprism.elliot.data.local.entity.Ingredient

data class FoodWithIngredientsModel(
    val foodName: String,
    val ingredients: List<Ingredient>
)