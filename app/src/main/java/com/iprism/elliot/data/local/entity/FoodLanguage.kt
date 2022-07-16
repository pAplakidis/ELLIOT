package com.iprism.elliot.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import org.jetbrains.annotations.NotNull

@Entity(primaryKeys = ["food_id", "lang"])
data class FoodLanguage(
    @ColumnInfo(name = "food_id")
    @NotNull
    val foodId: Long,
    @NotNull
    val lang: String,
    @ColumnInfo(name = "food_name")
    @NotNull
    val foodName: String
)