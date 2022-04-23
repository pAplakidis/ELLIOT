package com.iprism.elliot.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import org.jetbrains.annotations.NotNull

@Entity(primaryKeys = ["history_id", "ingredient_id"])
data class HistoryIngredientCrossRef(
    @ColumnInfo(name = "history_id")
    @NotNull
    val historyId: Int,
    @ColumnInfo(name = "ingredient_id")
    @NotNull
    val ingredientId: Int
)
