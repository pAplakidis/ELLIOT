package com.iprism.elliot.data.local

import androidx.room.*
import com.iprism.elliot.data.local.entity.*
import com.iprism.elliot.domain.model.HistoryModel
import com.iprism.elliot.domain.model.NutrientsModel
import com.iprism.elliot.domain.model.SuggestionModel

@Dao
interface FoodDao {
    @Query(
        "SELECT * FROM FoodLanguage " +
                "JOIN FoodIngredientCrossRef ON FoodLanguage.food_id = FoodIngredientCrossRef.food_id AND FoodLanguage.lang = FoodIngredientCrossRef.lang " +
                "JOIN Ingredient ON Ingredient.ingredient_id = FoodIngredientCrossRef.ingredient_id AND Ingredient.lang = FoodIngredientCrossRef.lang " +
                "WHERE food_name LIKE :foodName"
    )
    @Throws(NoSuchElementException::class)
    suspend fun getFoodWithIngredients(foodName: String): Map<FoodLanguage, List<Ingredient>>

    @Query("SELECT * FROM History WHERE food_name = :foodName AND date = :date AND time = :time")
    suspend fun getHistoryWithIngredients(
        foodName: String,
        date: String,
        time: String
    ): HistoryWithIngredients

    @Query("SELECT MAX(history_id) FROM History")
    suspend fun getLatestFoodHistoryId(): Long

    @Query("SELECT sentence FROM Suggestion")
    suspend fun getAllSuggestions(): List<String>

    @Insert(entity = Suggestion::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSuggestion(suggestion: SuggestionModel)

    @Insert(entity = History::class)
    suspend fun insertFood(history: HistoryModel)

    @Insert
    suspend fun insertHistoryIngredients(historyIngredient: HistoryIngredientCrossRef)

    @Query(
        "SELECT * FROM Ingredient " +
                "JOIN HistoryIngredientCrossRef ON Ingredient.ingredient_id = HistoryIngredientCrossRef.ingredient_id " +
                "JOIN History ON History.history_id = HistoryIngredientCrossRef.history_id " +
                "WHERE lang = :lang"
    )
    suspend fun getAllHistoryWithIngredients(lang: String): Map<History, List<Ingredient>>

    @Query(
        "SELECT * FROM Ingredient " +
                "JOIN HistoryIngredientCrossRef ON Ingredient.ingredient_id = HistoryIngredientCrossRef.ingredient_id " +
                "JOIN History ON History.history_id = HistoryIngredientCrossRef.history_id " +
                "WHERE lang = :lang AND date = :dateChosen"
    )
    suspend fun getHistoryWithIngredientsDate(
        lang: String,
        dateChosen: String
    ): Map<History, List<Ingredient>>

    @Query(
        "SELECT " +
                "ROUND(SUM(protein), 2) as protein, " +
                "ROUND(SUM(fat), 2) as fat, " +
                "ROUND(SUM(carbohydrate), 2) as carbohydrate, " +
                "ROUND(SUM(fiber), 2) as fiber, " +
                "ROUND(SUM(sodium), 2) as sodium, " +
                "ROUND(SUM(sugar), 2) as sugar " +
                "FROM Food " +
                "JOIN FoodLanguage ON FoodLanguage.food_id = Food.food_id " +
                "JOIN History ON History.food_name = FoodLanguage.food_name " +
                "WHERE date >= ( SELECT DATE('now', '-7 day') ) AND date < ( SELECT DATE('now') )"
    )
    suspend fun getLastSevenDaysNutrients(): NutrientsModel

    @Query(
        "SELECT " +
                "ROUND(SUM(protein), 2) as protein, " +
                "ROUND(SUM(fat), 2) as fat, " +
                "ROUND(SUM(carbohydrate), 2) as carbohydrate, " +
                "ROUND(SUM(fiber), 2) as fiber, " +
                "ROUND(SUM(sodium), 2) as sodium, " +
                "ROUND(SUM(sugar), 2) as sugar " +
                "FROM Food " +
                "JOIN FoodLanguage ON FoodLanguage.food_id = Food.food_id " +
                "JOIN History ON History.food_name = FoodLanguage.food_name " +
                "WHERE date BETWEEN :dateStart AND :dateEnd"
    )
    suspend fun getNutrients(dateStart: String, dateEnd: String): NutrientsModel

    @Query(
        "SELECT " +
                "ROUND(SUM(protein), 2) as protein, " +
                "ROUND(SUM(fat), 2) as fat, " +
                "ROUND(SUM(carbohydrate), 2) as carbohydrate, " +
                "ROUND(SUM(fiber), 2) as fiber, " +
                "ROUND(SUM(sodium), 2) as sodium, " +
                "ROUND(SUM(sugar), 2) as sugar " +
                "FROM Food " +
                "JOIN FoodLanguage ON FoodLanguage.food_id = Food.food_id " +
                "JOIN History ON History.food_name = FoodLanguage.food_name " +
                "WHERE date BETWEEN :dateStart AND :dateEnd AND meal = :meal"
    )
    suspend fun getNutrientsByMeal(dateStart: String, dateEnd: String, meal: String): NutrientsModel
}