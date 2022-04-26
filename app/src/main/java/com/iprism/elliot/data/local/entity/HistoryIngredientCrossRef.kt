package com.iprism.elliot.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity
data class HistoryIngredientCrossRef(
    @PrimaryKey
    @ColumnInfo(name = "history_ingredient_id")
    val historyIngredientId: Int? = null,
    @ColumnInfo(name = "history_id")
    @NotNull
    val historyId: Int,
    @ColumnInfo(name = "ingredient_id")
    @NotNull
    val ingredientId: Int
)
