package com.iprism.elliot.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity
data class Suggestion(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "suggestion_id")
    @NotNull
    val suggestionId: Long,
    @NotNull
    val sentence: String
)