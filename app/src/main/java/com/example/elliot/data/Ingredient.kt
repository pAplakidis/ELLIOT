package com.example.elliot.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity
data class Ingredient(
    @PrimaryKey
    @ColumnInfo(name = "ingredient_id")
    @NotNull
    val ingredientId: Int,
    @ColumnInfo(name = "ingredient_name")
    @NotNull
    val ingredientName: String
)