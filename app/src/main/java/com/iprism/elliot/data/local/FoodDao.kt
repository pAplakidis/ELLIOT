package com.iprism.elliot.data.local

import androidx.room.*
import com.iprism.elliot.data.local.entity.History
import com.iprism.elliot.data.local.entity.HistoryIngredientCrossRef
import com.iprism.elliot.domain.model.HistoryModel

@Dao
interface FoodDao {
    @Transaction
    @Query("SELECT * FROM Food WHERE food_name LIKE :foodName")
    suspend fun getFoodWithIngredients(foodName: String): FoodWithIngredients

    @Transaction
    @Query("SELECT * FROM History WHERE food_name = :foodName AND date = :date AND time = :time")
    suspend fun getHistoryWithIngredients(
        foodName: String,
        date: String,
        time: String
    ): HistoryWithIngredients

    @Query("SELECT MAX(history_id) FROM History")
    suspend fun getLatestFoodHistoryId(): Int

    @Insert(entity = History::class)
    suspend fun insertFood(history: HistoryModel)

    @Insert
    suspend fun insertHistoryIngredients(historyIngredient: HistoryIngredientCrossRef)

//    @Transaction
//    @Query("SELECT * FROM Ingredient")
//    suspend fun getIngredientWithFoods(): List<IngredientWithFoods>

    @Transaction
    @Query("SELECT * FROM History")
    suspend fun getAllHistoryWithIngredients(): List<HistoryWithIngredients>
}