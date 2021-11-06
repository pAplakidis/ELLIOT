package com.example.elliot.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class FoodWithIngredients(
    @Embedded val food: Food,
    @Relation(
        parentColumn = "food_id",
        entityColumn = "ingredient_id",
        associateBy = Junction(FoodIngredientCrossRef::class)
    )
    val ingredients: List<Ingredient>
)
