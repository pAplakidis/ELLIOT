package com.iprism.elliot.data.repository

import com.iprism.elliot.data.local.entity.*
import com.iprism.elliot.domain.model.HistoryModel
import com.iprism.elliot.domain.model.NutrientsModel
import com.iprism.elliot.domain.model.SuggestionModel
import kotlinx.coroutines.flow.Flow

interface FoodRepository {
    fun getFoodWithIngredients(foodName: String): Flow<List<Ingredient>>

    suspend fun getLatestFoodHistoryId(): Int

    suspend fun getAllSuggestions(): Flow<List<String>>

    suspend fun insertFood(history: HistoryModel)

    suspend fun insertSuggestion(suggestion: SuggestionModel)

    suspend fun insertHistoryIngredients(historyIngredient: HistoryIngredientCrossRef)

//    fun getHistoryWithIngredients(
//        foodName: String,
//        date: String,
//        time: String
//    ): Flow<HistoryWithIngredients>

    fun getAllHistoryWithIngredients(lang: String): Flow<Map<History, List<Ingredient>>>

    suspend fun getLastSevenDaysNutrients(): NutrientsModel

    suspend fun getNutrients(dateStart: String, dateEnd: String): Flow<NutrientsModel>

    suspend fun getNutrientsByMeal(dateStart: String, dateEnd: String, meal: String): Flow<NutrientsModel>
}