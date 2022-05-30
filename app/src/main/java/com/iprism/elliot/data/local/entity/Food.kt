package com.iprism.elliot.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity
data class Food(
    @PrimaryKey
    @ColumnInfo(name = "food_id")
    @NotNull
    val foodId: Int,
    @NotNull
    val protein: Double,
    @NotNull
    val fat: Double,
    @NotNull
    val carbohydrate: Double,
    @NotNull
    val fiber: Double,
    @NotNull
    val sodium: Double
) {
//    fun toFoodModel(): FoodModel {
//        return FoodModel(
//            foodName = foodName
//        )
//    }
}