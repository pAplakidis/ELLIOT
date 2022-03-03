package com.example.elliot.data.local

import androidx.room.*
import com.example.elliot.data.local.entity.Food

@Dao
interface FoodDao {
    @Transaction
    @Query("SELECT * FROM Food")
    suspend fun getFoodWithIngredients(): List<FoodWithIngredients>

    @Transaction
    @Query("SELECT * FROM Ingredient")
    suspend fun getIngredientWithFoods(): List<IngredientWithFoods>

    @Insert(entity = Food::class)
    suspend fun insertFood(food: Food)

    @Query("SELECT * FROM Food")
    suspend fun getFoods(): List<Food>
}