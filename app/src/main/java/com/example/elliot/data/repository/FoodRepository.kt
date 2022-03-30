package com.example.elliot.data.repository

import com.example.elliot.data.local.FoodWithIngredients
import com.example.elliot.data.local.HistoryWithIngredients
import com.example.elliot.data.local.entity.HistoryIngredient
import com.example.elliot.domain.model.HistoryModel
import kotlinx.coroutines.flow.Flow

interface FoodRepository {
    fun getFoodWithIngredients(foodName: String): Flow<FoodWithIngredients>

    suspend fun getLatestFoodId(): Int

    suspend fun getFoodHistoryId(foodName: String): Int

    suspend fun insertFood(history: HistoryModel)

    suspend fun insertHistoryIngredients(historyIngredient: HistoryIngredient)

    suspend fun getHistoryWithIngredients(foodName: String): Flow<HistoryWithIngredients>
}