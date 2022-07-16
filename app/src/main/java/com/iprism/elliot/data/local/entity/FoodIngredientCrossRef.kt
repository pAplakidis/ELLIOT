package com.iprism.elliot.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import org.jetbrains.annotations.NotNull

@Entity(primaryKeys = ["food_id", "ingredient_id", "lang"])
data class FoodIngredientCrossRef(
    @ColumnInfo(name = "food_id")
    @NotNull
    val foodId: Long,
    @ColumnInfo(name = "ingredient_id")
    @NotNull
    val ingredientId: Long,
    @NotNull
    val lang: String
)