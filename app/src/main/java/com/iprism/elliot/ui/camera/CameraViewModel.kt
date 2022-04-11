package com.iprism.elliot.ui.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.elliot.data.local.entity.HistoryIngredient
import com.iprism.elliot.data.repository.FoodRepository
import com.iprism.elliot.domain.model.HistoryModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val repository: FoodRepository
) : ViewModel() {

    private val ingredientsToIDs = mutableMapOf<String, Int>()
    var foodName = ""

    // Backing property to avoid state updates from other classes
    private val _ingredientsListUiState =
        MutableStateFlow(IngredientListUiState(emptyArray(), BooleanArray(0)))

    // The UI collects from this StateFlow to get its state updates
    val ingredientsListUiState = _ingredientsListUiState.asStateFlow()

    fun onEvent(event: CameraEvent) {
        when (event) {
            is CameraEvent.OnCameraButtonClick -> {
                foodName = event.foodName
            }
            is CameraEvent.OnConfirmationDialogOkClick -> {
                ingredientsListUiState.value.checked.zip(ingredientsListUiState.value.ingredients) { checked, ingredient ->
                    viewModelScope.launch {
                        if (checked) repository.insertHistoryIngredients(
                            HistoryIngredient(
                                historyId = repository.getFoodHistoryId(foodName),
                                ingredientId = ingredientsToIDs[ingredient]!!
                            )
                        )
                    }
                }
            }
            is CameraEvent.OnMealEntryDialogOkClick -> {
                insertFoodAndGetIngredients()
            }
            is CameraEvent.OnPredictionCheckDialogYesClick -> {
                insertFoodAndGetIngredients()
            }
        }
    }

    private fun insertFoodAndGetIngredients() {
        viewModelScope.launch {
            repository.insertFood(HistoryModel(food_name = foodName))
            getIngredients()
        }
    }

    private suspend fun getIngredients() {
        repository.getFoodWithIngredients(foodName).collect {
            _ingredientsListUiState.value = ingredientsListUiState.value.copy(
                ingredients = it.toFoodWithIngredientsModel().ingredients.toTypedArray(),
                checked = BooleanArray(it.toFoodWithIngredientsModel().ingredients.toTypedArray().size) { true }
            )

            it.ingredients.forEach { ingredient ->
                ingredientsToIDs[ingredient.ingredientName] = ingredient.ingredientId
            }
        }
    }

    data class IngredientListUiState(
        val ingredients: Array<String>,
        val checked: BooleanArray
    )
}