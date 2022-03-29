package com.example.elliot.data.repository

import com.example.elliot.data.local.FoodDao
import com.example.elliot.data.local.FoodWithIngredients
import com.example.elliot.data.local.entity.HistoryIngredient
import com.example.elliot.domain.model.HistoryModel
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
}