package com.example.elliot.domain.model

import com.example.elliot.data.local.entity.Food

data class FoodModel(
    var foodId: Int = 0,
    val foodName: String
) {
    fun toFood(food: FoodModel): Food {
        return Food(
            foodName = foodName,
            foodId = food.foodId
        )
    }
}
