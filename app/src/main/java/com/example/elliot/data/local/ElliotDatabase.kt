package com.example.elliot.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.elliot.data.local.entity.Food
import com.example.elliot.data.local.entity.FoodIngredientCrossRef
import com.example.elliot.data.local.entity.Ingredient

@Database(
    entities = [Food::class, Ingredient::class, FoodIngredientCrossRef::class],
    version = 1,
    exportSchema = false
)
abstract class ElliotDatabase : RoomDatabase() {

    abstract val dao: FoodDao

//    companion object {
//        @Volatile
//        private var INSTANCE: ElliotRoomDatabase? = null
//
//        fun getDatabase(context: Context): ElliotRoomDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    ElliotRoomDatabase::class.java,
//                    "elliot_database"
//                )
//                    .createFromAsset("database/elliot.db")
//                    .build()
//                INSTANCE = instance
//                return instance
//            }
//        }
//    }
}