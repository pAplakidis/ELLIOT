package com.iprism.elliot.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import org.jetbrains.annotations.NotNull

@Entity(primaryKeys = ["ingredient_id", "lang"])
data class Ingredient(
    @ColumnInfo(name = "ingredient_id")
    @NotNull
    val ingredientId: Long,
    @NotNull
    val lang: String,
    @ColumnInfo(name = "ingredient_name")
    @NotNull
    val ingredientName: String
)