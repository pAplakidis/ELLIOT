package com.iprism.elliot.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iprism.elliot.data.local.entity.*

@Database(
    entities = [Food::class, FoodLanguage::class, Ingredient::class,
        FoodIngredientCrossRef::class, History::class, HistoryIngredientCrossRef::class, Suggestion::class],
    version = 1,
    exportSchema = false
)
abstract class ElliotDatabase : RoomDatabase() {
    abstract val dao: FoodDao
}