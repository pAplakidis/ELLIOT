package com.iprism.elliot.domain.model

data class FoodModel(
//    var foodId: Int = 0,
    val foodName: String,
    val foodDate: String,
    val foodIngredients: List<String>
) {
//    fun toFood(food: FoodModel): Food {
//        return Food(
//            foodName = foodName,
//            foodId = food.foodId
//        )
//    }
}
