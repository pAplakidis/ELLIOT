package com.example.elliot.data.local

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.elliot.data.local.entity.Food
import com.example.elliot.data.local.entity.FoodIngredientCrossRef
import com.example.elliot.data.local.entity.Ingredient

data class FoodWithIngredients(
    @Embedded val food: Food,
    @Relation(
        parentColumn = "food_id",
        entityColumn = "ingredient_id",
        associateBy = Junction(FoodIngredientCrossRef::class)
    )
    val ingredients: List<Ingredient>
)
