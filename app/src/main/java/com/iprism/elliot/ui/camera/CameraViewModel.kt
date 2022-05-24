package com.iprism.elliot.ui.camera

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.elliot.data.local.entity.HistoryIngredientCrossRef
import com.iprism.elliot.data.repository.FoodRepository
import com.iprism.elliot.domain.model.HistoryModel
import com.iprism.elliot.util.Utils.capitalizeWords
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val repository: FoodRepository
) : ViewModel() {

    private val ingredientsToIDs = mutableMapOf<String, Int>()

    var foodName = ""
    var date = ""
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
                val calendarInstance = Calendar.getInstance()
                val year = calendarInstance.get(Calendar.YEAR).toString()
                val month = (calendarInstance.get(Calendar.MONTH) + 1).toString()
                val day = calendarInstance.get(Calendar.DAY_OF_MONTH).toString()
                date = "$day/$month/$year"

                var hour = calendarInstance.get(Calendar.HOUR_OF_DAY).toString()
                var minutes = calendarInstance.get(Calendar.MINUTE).toString()
                var seconds = calendarInstance.get(Calendar.SECOND).toString()

                if (hour.length < 2) {
                    hour = "0$hour"
                }

                if (minutes.length < 2) {
                    minutes = "0$minutes"
                }

                if (seconds.length < 2) {
                    seconds = "0$seconds"
                }

                time = "$hour:$minutes:$seconds"

                meal = when {
                    hour.toInt() < 12 -> {
                        "Breakfast"
                    }
                    hour.toInt() < 18 -> {
                        "Lunch"
                    }
                    else -> {
                        "Dinner"
                    }
                }
            }
            is CameraEvent.OnConfirmationDialogOkClick -> {
                insertFood()

                _ingredientsListUiState.value.checked.zip(_ingredientsListUiState.value.ingredients) { checked, ingredient ->
                    viewModelScope.launch {
                        val id = repository.getLatestFoodHistoryId()
                        if (checked) repository.insertHistoryIngredients(
                            HistoryIngredientCrossRef(
                                historyId = id,
                                ingredientId = ingredientsToIDs[ingredient]!!
                            )
                        )
                    }
                }
                // PARE TA NUTRIENTS EDW, KALESE TO RULESET KAI ME MIA FOR TSEKARE KATHE RULE. UPDATE TA SUGGESTIONS ANALOGWS
            }
            is CameraEvent.OnMealEntryDialogOkClick -> {
                foodName = foodName.capitalizeWords()
                getIngredients()
            }
            is CameraEvent.OnPredictionCheckDialogYesClick -> {
                foodName = event.foodName
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