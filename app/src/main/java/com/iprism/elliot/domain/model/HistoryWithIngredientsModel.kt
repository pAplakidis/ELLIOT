package com.iprism.elliot.domain.model

import com.iprism.elliot.data.local.entity.HistoryIngredient

data class HistoryWithIngredientsModel(
    val foodName: String,
    val foodDate: String?,
    val foodTime: String?,
    val meal: String?,
    val ingredients: List<HistoryIngredient>
)