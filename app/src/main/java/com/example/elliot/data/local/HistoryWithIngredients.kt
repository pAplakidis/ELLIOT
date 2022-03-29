package com.example.elliot.data.local

import androidx.room.Embedded
import androidx.room.Relation
import com.example.elliot.data.local.entity.History
import com.example.elliot.data.local.entity.HistoryIngredient

data class HistoryWithIngredients(
    @Embedded val history: History,
    @Relation(
        parentColumn = "history_id",
        entityColumn = "history_id"
    )
    val ingredients: List<HistoryIngredient>
)
