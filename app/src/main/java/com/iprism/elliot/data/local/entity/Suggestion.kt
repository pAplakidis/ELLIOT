package com.iprism.elliot.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity
data class Suggestion(
    @ColumnInfo(name = "suggestion_id")
    @PrimaryKey(autoGenerate = true)
    @NotNull
    val suggestionId: Int,
    @NotNull
    val sentence: String
)