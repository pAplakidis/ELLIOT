package com.iprism.elliot.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import org.jetbrains.annotations.NotNull

@Entity(primaryKeys = ["food_id", "ingredient_id", "lang"])
data class FoodIngredientCrossRef(
    @ColumnInfo(name = "food_id")
    @NotNull
    val foodId: Int,
    @ColumnInfo(name = "ingredient_id")
    @NotNull
    val ingredientId: Int,
    @NotNull
    val lang: String
)