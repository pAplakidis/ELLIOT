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
    @Query("SELECT * FROM History WHERE food_name = :foodName")
    suspend fun getHistoryWithIngredients(foodName: String): HistoryWithIngredients

    @Query("SELECT history_id FROM History WHERE food_name = :foodName")
    suspend fun getFoodHistoryId(foodName: String): Int

    @Insert(entity = History::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(history: HistoryModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistoryIngredients(historyIngredient: HistoryIngredientCrossRef)

//    @Transaction
//    @Query("SELECT * FROM Ingredient")
//    suspend fun getIngredientWithFoods(): List<IngredientWithFoods>

    @Query("SELECT MAX(food_id) FROM Food")
    suspend fun getLatestFoodId(): Int

    @Transaction
    @Query("SELECT * FROM History")
    suspend fun getAllHistoryWithIngredients(): List<HistoryWithIngredients>
}