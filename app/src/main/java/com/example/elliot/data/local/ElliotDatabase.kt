package com.example.elliot.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.elliot.data.local.entity.*

@Database(
    entities = [Food::class, Ingredient::class, FoodIngredientCrossRef::class, History::class, HistoryIngredient::class],
    version = 1,
    exportSchema = false
)
abstract class ElliotDatabase : RoomDatabase() {

    abstract val dao: FoodDao
}