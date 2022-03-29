package com.example.elliot.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity
data class History(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "history_id")
    @NotNull
    val historyId: Int,
    @ColumnInfo(name = "food_name")
    @NotNull
    val foodName: String,
    // @NotNull
    val meal: String?,
    // @NotNull
    val date: String?,
    // @NotNull
    val time: String?
)
