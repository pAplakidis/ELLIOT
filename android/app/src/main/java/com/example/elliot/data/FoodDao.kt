package com.example.elliot.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface FoodDao {
    @Transaction
    @Query("SELECT * FROM Food")
    suspend fun getFoodWithIngredients(): List<FoodWithIngredients>

    @Transaction
    @Query("SELECT * FROM Ingredient")
    suspend fun getIngredientWithFoods(): List<IngredientWithFoods>
}