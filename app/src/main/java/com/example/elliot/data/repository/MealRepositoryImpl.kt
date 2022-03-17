package com.example.elliot.data.repository

import com.example.elliot.domain.model.CardModel
import com.example.elliot.domain.model.FoodModel
import kotlinx.coroutines.flow.Flow

class MealRepositoryImpl (
) : FoodRepository {
    override suspend fun insertFood(foodModel: FoodModel) {
        TODO("Not yet implemented")
    }

    override fun getFoods(): Flow<List<FoodModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun getLatestFoodId(): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getHistoryInformation(): List<CardModel> {
        TODO("Not yet implemented")
    }

}