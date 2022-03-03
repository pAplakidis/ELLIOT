package com.example.elliot.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.elliot.domain.model.Food
import org.jetbrains.annotations.NotNull

@Entity
data class Food(
    @PrimaryKey
    @ColumnInfo(name = "food_id")
    @NotNull
    val foodId: Int = 500,
    @ColumnInfo(name = "food_name")
    @NotNull
    val foodName: String
) {
    fun toFood(): Food {
        return Food(
            foodName = foodName
        )
    }
}