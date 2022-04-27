package com.iprism.elliot.ui.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.elliot.data.local.entity.HistoryIngredientCrossRef
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
    var date = ""
    private var hour = ""
    private var minutes = ""
    private var seconds = ""
    var meal = ""
    var time = ""

    // Backing property to avoid state updates from other classes
    private val _ingredientsListUiState =
        MutableStateFlow(IngredientListUiState(emptyArray(), BooleanArray(0)))
//    val ingredientsListUiState = _ingredientsListUiState.asStateFlow()

    private val _oneTimeIngredientsListUiState = MutableSharedFlow<IngredientListUiState>()

    // The UI collects from this SharedFlow to get its state updates
    val oneTimeIngredientsListUiState = _oneTimeIngredientsListUiState.asSharedFlow()

    fun onEvent(event: CameraEvent) {
        when (event) {
            is CameraEvent.OnCameraButtonClick -> {
                foodName = event.foodName
                date = event.date
                hour = event.hour
                minutes = event.minutes
                seconds = event.seconds
                time = "$hour:$minutes:$seconds"

                if (event.hour.toInt() < 12) {
                    meal = "Breakfast"
                } else if (event.hour.toInt() < 18) {
                    meal = "Lunch"
                } else {
                    meal = "Dinner"
                }
            }
            is CameraEvent.OnConfirmationDialogOkClick -> {
                insertFood()

                _ingredientsListUiState.value.checked.zip(_ingredientsListUiState.value.ingredients) { checked, ingredient ->
                    viewModelScope.launch {
                        if (checked) repository.insertHistoryIngredients(
                            HistoryIngredientCrossRef(
                                historyId = repository.getFoodHistoryId(foodName, date, time),
                                ingredientId = ingredientsToIDs[ingredient]!!
                            )
                        )
                    }
                }
            }
            is CameraEvent.OnMealEntryDialogOkClick -> {
                getIngredients()
            }
            is CameraEvent.OnPredictionCheckDialogYesClick -> {
                getIngredients()
            }
        }
    }

    private fun insertFood() {
        viewModelScope.launch {
            repository.insertFood(
                HistoryModel(
                    food_name = foodName,
                    date = date,
                    meal = meal,
                    time = time
                )
            )
        }
    }

    private fun getIngredients() {
        viewModelScope.launch {
            repository.getFoodWithIngredients(foodName).collect {
                _ingredientsListUiState.value = _ingredientsListUiState.value.copy(
                    ingredients = it.toFoodWithIngredientsModel().ingredients.toTypedArray(),
                    checked = BooleanArray(it.toFoodWithIngredientsModel().ingredients.toTypedArray().size) { true }
                )

                it.ingredients.forEach { ingredient ->
                    ingredientsToIDs[ingredient.ingredientName] = ingredient.ingredientId
                }

                _oneTimeIngredientsListUiState.emit(_ingredientsListUiState.value)
            }
        }
    }

    data class IngredientListUiState(
        val ingredients: Array<String>,
        val checked: BooleanArray
    )
}