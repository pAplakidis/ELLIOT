package com.iprism.elliot.data.repository

import com.iprism.elliot.data.local.FoodWithIngredients
import com.iprism.elliot.data.local.HistoryWithIngredients
import com.iprism.elliot.data.local.entity.HistoryIngredientCrossRef
import com.iprism.elliot.domain.model.HistoryModel
import kotlinx.coroutines.flow.Flow

interface FoodRepository {
    fun getFoodWithIngredients(foodName: String): Flow<FoodWithIngredients>

    suspend fun getLatestFoodId(): Int

    suspend fun getFoodHistoryId(foodName: String, date: String, time: String): Int

    suspend fun insertFood(history: HistoryModel)

    suspend fun insertHistoryIngredients(historyIngredient: HistoryIngredientCrossRef)

    fun getHistoryWithIngredients(
        foodName: String,
        date: String,
        time: String
    ): Flow<HistoryWithIngredients>

    fun getAllHistoryWithIngredients(): Flow<List<HistoryWithIngredients>>
}