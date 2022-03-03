package com.example.elliot.data.repository

import com.example.elliot.data.local.FoodDao
import com.example.elliot.domain.model.Food
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FoodRepositoryImpl(
    private val dao: FoodDao
) : FoodRepository {

    override suspend fun insertFood(food: Food) {
        dao.insertFood(food.toFoodEntity())
    }

    override fun getFoods(): Flow<List<Food>> = flow {
        emit(dao.getFoods().map { it.toFood() })
    }
 }