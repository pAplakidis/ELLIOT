package com.example.elliot.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class IngredientWithFoods(
    @Embedded val ingredient: Ingredient,
    @Relation(
        parentColumn = "ingredient_id",
        entityColumn = "food_id",
        associateBy = Junction(FoodIngredientCrossRef::class)
    )
    val foods: List<Food>
)
