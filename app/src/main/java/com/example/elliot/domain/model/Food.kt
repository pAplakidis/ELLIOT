package com.example.elliot.domain.model

import com.example.elliot.data.local.entity.Food

data class Food(
    val foodName: String
) {
    fun toFoodEntity(): Food {
        return Food(
            foodName = foodName
        )
    }
}
