package com.example.elliot.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Food::class, Ingredient::class, FoodIngredientCrossRef::class],
    version = 1,
    exportSchema = false
)
abstract class ElliotRoomDatabase : RoomDatabase() {

    abstract fun foodDao(): FoodDao

    companion object {
        @Volatile
        private var INSTANCE: ElliotRoomDatabase? = null

        fun getDatabase(context: Context): ElliotRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ElliotRoomDatabase::class.java,
                    "elliot_database"
                )
                    .createFromAsset("database/elliot.db")
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}