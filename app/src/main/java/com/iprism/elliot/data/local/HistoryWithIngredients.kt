package com.iprism.elliot.data.local

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.iprism.elliot.data.local.entity.History
import com.iprism.elliot.data.local.entity.HistoryIngredientCrossRef
import com.iprism.elliot.data.local.entity.Ingredient

data class HistoryWithIngredients(
    @Embedded val history: History,
    @Relation(
        parentColumn = "history_id",
        entityColumn = "ingredient_id",
        associateBy = Junction(HistoryIngredientCrossRef::class)
    )
    val ingredients: List<Ingredient>
)