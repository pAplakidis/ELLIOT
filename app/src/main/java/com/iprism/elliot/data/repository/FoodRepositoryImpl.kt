package com.iprism.elliot.data.repository

import com.iprism.elliot.data.local.FoodDao
import com.iprism.elliot.data.local.FoodWithIngredients
import com.iprism.elliot.data.local.HistoryWithIngredients
import com.iprism.elliot.data.local.entity.HistoryIngredient
import com.iprism.elliot.domain.model.HistoryModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FoodRepositoryImpl(
    private val dao: FoodDao,
) : FoodRepository {
    override fun getFoodWithIngredients(foodName: String): Flow<FoodWithIngredients> = flow {
        emit(dao.getFoodWithIngredients(foodName))
    }

    override suspend fun getLatestFoodId(): Int {
        return dao.getLatestFoodId()
    }

    override suspend fun getFoodHistoryId(foodName: String): Int {
        return dao.getFoodHistoryId(foodName)
    }

    override suspend fun insertFood(history: HistoryModel) {
        dao.insertFood(history)
    }

    override suspend fun insertHistoryIngredients(historyIngredient: HistoryIngredient) {
        dao.insertHistoryIngredients(historyIngredient)
    }

    override fun getAllHistoryWithIngredients(): Flow<List<HistoryWithIngredients>> = flow {
        emit(dao.getAllHistoryWithIngredients())
    }

    override fun getHistoryWithIngredients(foodName: String): Flow<HistoryWithIngredients> =
        flow {
            emit(dao.getHistoryWithIngredients(foodName))
        }
}