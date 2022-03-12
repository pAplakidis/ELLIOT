package com.example.elliot.data.repository

import com.example.elliot.domain.model.FoodModel
import kotlinx.coroutines.flow.Flow

interface FoodRepository {
    suspend fun insertFood(foodModel: FoodModel)

    fun getFoods(): Flow<List<FoodModel>>

    suspend fun getLatestFoodId(): Int
}