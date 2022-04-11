package com.iprism.elliot.data.local

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.iprism.elliot.data.local.entity.Food
import com.iprism.elliot.data.local.entity.FoodIngredientCrossRef
import com.iprism.elliot.data.local.entity.Ingredient
import com.iprism.elliot.domain.model.FoodWithIngredientsModel

data class FoodWithIngredients(
    @Embedded val food: Food,
    @Relation(
        parentColumn = "food_id",
        entityColumn = "ingredient_id",
        associateBy = Junction(FoodIngredientCrossRef::class)
    )
    val ingredients: List<Ingredient>
) {
    fun toFoodWithIngredientsModel(): FoodWithIngredientsModel {
        return FoodWithIngredientsModel(
            foodName = food.foodName,
            ingredients = ingredients.map { it.ingredientName }
        )
    }
}
