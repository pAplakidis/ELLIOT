package com.iprism.elliot.data.local

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.iprism.elliot.data.local.entity.Food
import com.iprism.elliot.data.local.entity.FoodIngredientCrossRef
import com.iprism.elliot.data.local.entity.Ingredient

data class IngredientWithFoods(
    @Embedded val ingredient: Ingredient,
    @Relation(
        parentColumn = "ingredient_id",
        entityColumn = "food_id",
        associateBy = Junction(FoodIngredientCrossRef::class)
    )
    val foods: List<Food>
)
