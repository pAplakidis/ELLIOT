package com.iprism.elliot.data.repository

import android.content.res.Resources
import com.iprism.elliot.R
import com.iprism.elliot.data.local.FoodDao
import com.iprism.elliot.data.local.entity.*
import com.iprism.elliot.domain.model.HistoryModel
import com.iprism.elliot.domain.model.NutrientsModel
import com.iprism.elliot.domain.model.SuggestionModel
import com.iprism.elliot.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FoodRepositoryImpl(
    private val dao: FoodDao,
    private val resources: Resources,
) : FoodRepository {
    override fun getFoodWithIngredients(foodName: String): Flow<Resource<List<Ingredient>>> =
        flow {
            try {
                emit(Resource.Success(dao.getFoodWithIngredients(foodName).values.single()))
            } catch (e: NoSuchElementException) {
                emit(Resource.Error(resources.getString(R.string.not_supported_food)))
            }
        }

    override suspend fun getLatestFoodHistoryId(): Long {
        return dao.getLatestFoodHistoryId()
    }

    override fun getAllSuggestions(): Flow<List<String>> =
        flow {
            emit(dao.getAllSuggestions())
        }

    override suspend fun insertFood(history: HistoryModel) {
        dao.insertFood(history)
    }

    override suspend fun insertSuggestion(suggestion: SuggestionModel) {
        dao.insertSuggestion(suggestion)
    }

    override suspend fun insertHistoryIngredients(historyIngredient: HistoryIngredientCrossRef) {
        dao.insertHistoryIngredients(historyIngredient)
    }

    override fun getAllHistoryWithIngredients(lang: String): Flow<Map<History, List<Ingredient>>> =
        flow {
            emit(dao.getAllHistoryWithIngredients(lang))
        }

    override fun getHistoryWithIngredientsDate(
        lang: String,
        dateChosen: String
    ): Flow<Map<History, List<Ingredient>>> =
        flow {
            emit(dao.getHistoryWithIngredientsDate(lang, dateChosen))
        }

    override suspend fun getLastSevenDaysNutrients(): NutrientsModel {
        return dao.getLastSevenDaysNutrients()
    }

    override fun getNutrients(dateStart: String, dateEnd: String): Flow<NutrientsModel> =
        flow {
            emit(dao.getNutrients(dateStart, dateEnd))
        }

    override fun getNutrientsByMeal(
        dateStart: String,
        dateEnd: String,
        meal: String
    ): Flow<NutrientsModel> =
        flow {
            emit(dao.getNutrientsByMeal(dateStart, dateEnd, meal))
        }

    override suspend fun getFoodIdByName(foodName: String): Long {
        return dao.getFoodIdByName(foodName)
    }

    override suspend fun getFoodNameByIdAndLocale(foodId: Long, locale: String): String {
        return dao.getFoodNameByIdAndLocale(foodId, locale)
    }
}