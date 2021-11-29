package com.example.elliot.data

import com.example.elliot.model.FoodDetail
import com.example.elliot.model.Meal

class Datasource {
    fun loadMeals(): List<Meal> {
        return listOf(
            Meal(
                "Breakfast", listOf(
                    FoodDetail("Apple", "12:00"),
                    FoodDetail("Milk", "12:00")
                )
            ),
            Meal(
                "Lunch", listOf(
                    FoodDetail("Chicken", "15:00")
                )
            ),
            Meal(
                "Dinner", listOf(
                    FoodDetail("Toast", "21:00")
                )
            )
        )
    }
}