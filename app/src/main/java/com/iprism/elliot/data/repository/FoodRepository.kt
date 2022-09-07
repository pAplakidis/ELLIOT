package com.iprism.elliot.data.repository

import com.iprism.elliot.data.local.entity.*
import com.iprism.elliot.domain.model.HistoryModel
import com.iprism.elliot.domain.model.NutrientsModel
import com.iprism.elliot.domain.model.SuggestionModel
import com.iprism.elliot.util.Resource
import kotlinx.coroutines.flow.Flow

interface FoodRepository {
    fun getFoodWithIngredients(foodName: String): Flow<Resource<List<Ingredient>>>

    suspend fun getLatestFoodHistoryId(): Long

    fun getAllSuggestions(): Flow<List<String>>

    suspend fun insertFood(history: HistoryModel)

    suspend fun insertSuggestion(suggestion: SuggestionModel)

    suspend fun insertHistoryIngredients(historyIngredient: HistoryIngredientCrossRef)

    fun getAllHistoryWithIngredients(lang: String): Flow<Map<History, List<Ingredient>>>

    fun getHistoryWithIngredientsDate(
        lang: String,
        dateChosen: String
    ): Flow<Map<History, List<Ingredient>>>

    suspend fun getLastSevenDaysNutrients(): NutrientsModel

    fun getNutrients(dateStart: String, dateEnd: String): Flow<NutrientsModel>

    fun getNutrientsByMeal(
        dateStart: String,
        dateEnd: String,
        meal: String
    ): Flow<NutrientsModel>

    suspend fun getFoodIdByName(foodName: String): Long

    suspend fun getFoodNameByIdAndLocale(foodId: Long, locale: String): String
}