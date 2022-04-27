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
    @ColumnInfo(name = "food_name")
    @NotNull
    val foodName: String
) {
//    fun toFoodModel(): FoodModel {
//        return FoodModel(
//            foodName = foodName
//        )
//    }
}