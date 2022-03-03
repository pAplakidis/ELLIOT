package com.example.elliot.data.repository

import com.example.elliot.domain.model.Food
import kotlinx.coroutines.flow.Flow

interface FoodRepository {
    suspend fun insertFood(food: Food)

    fun getFoods(): Flow<List<Food>>
}