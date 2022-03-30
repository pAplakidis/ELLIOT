package com.example.elliot.data.local

import androidx.room.Embedded
import androidx.room.Relation
import com.example.elliot.data.local.entity.History
import com.example.elliot.data.local.entity.HistoryIngredient
import com.example.elliot.domain.model.HistoryWithIngredientsModel

data class HistoryWithIngredients(
    @Embedded val history: History,
    @Relation(
        parentColumn = "history_id",
        entityColumn = "history_id"
    )
    val ingredients: List<HistoryIngredient>
) {
    fun toHistoryWithIngredientsModel(): HistoryWithIngredientsModel {
        return HistoryWithIngredientsModel(
            foodName = history.foodName,
            foodDate = history.date,
            foodTime = history.time,
            meal = history.meal,
            ingredients = ingredients
        )
    }
}
