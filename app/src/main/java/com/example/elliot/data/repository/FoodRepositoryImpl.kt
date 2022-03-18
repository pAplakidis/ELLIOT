package com.example.elliot.data.repository

import com.example.elliot.data.local.FoodDao
import com.example.elliot.domain.model.CardModel
import com.example.elliot.domain.model.FoodModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FoodRepositoryImpl(
    private val dao: FoodDao
) : FoodRepository {

    override suspend fun insertFood(foodModel: FoodModel) {
        dao.insertFood(foodModel.toFood(foodModel))
    }

    override fun getFoods(): Flow<List<FoodModel>> = flow {
        emit(dao.getFoods().map { it.toFoodModel() })
    }

    override suspend fun getLatestFoodId(): Int {
        return dao.getLatestFoodId()
    }

    override suspend fun getHistoryInformation(): List<CardModel> {
        // TODO("FAKE GIA NA MHN SKAEI.")
       return emptyList()
    }
}