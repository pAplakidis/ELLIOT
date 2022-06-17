package com.iprism.elliot.data.repository

import com.iprism.elliot.data.local.FoodDao
import com.iprism.elliot.data.local.entity.*
import com.iprism.elliot.domain.model.HistoryModel
import com.iprism.elliot.domain.model.NutrientsModel
import com.iprism.elliot.domain.model.SuggestionModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FoodRepositoryImpl(
    private val dao: FoodDao,
) : FoodRepository {
    override fun getFoodWithIngredients(foodName: String): Flow<List<Ingredient>> =
        flow {
            emit(dao.getFoodWithIngredients(foodName).values.single())
        }

    override suspend fun getLatestFoodHistoryId(): Int {
        return dao.getLatestFoodHistoryId()
    }

    override suspend fun getAllSuggestions(): Flow<List<String>> =
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

    override fun getHistoryWithIngredientsDate(lang: String, dateChosen: String): Flow<Map<History, List<Ingredient>>> =
        flow {
            emit(dao.getHistoryWithIngredientsDate(lang, dateChosen))
        }

    override suspend fun getLastSevenDaysNutrients(): NutrientsModel {
        return dao.getLastSevenDaysNutrients()
    }

    override suspend fun getNutrients(dateStart: String, dateEnd: String): Flow<NutrientsModel> =
        flow {
            emit(dao.getNutrients(dateStart, dateEnd))
        }

    override suspend fun getNutrientsByMeal(
        dateStart: String,
        dateEnd: String,
        meal: String
    ): Flow<NutrientsModel> =
        flow {
            emit(dao.getNutrientsByMeal(dateStart, dateEnd, meal))
        }

//    override fun getHistoryWithIngredients(
//        foodName: String,
//        date: String,
//        time: String
//    ): Flow<HistoryWithIngredients> =
//        flow {
//            emit(dao.getHistoryWithIngredients(foodName, date, time))
//        }
}