package com.iprism.elliot.ui.camera

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.elliot.data.local.entity.HistoryIngredientCrossRef
import com.iprism.elliot.data.repository.FoodRepository
import com.iprism.elliot.domain.model.HistoryModel
import com.iprism.elliot.domain.model.NutrientsModel
import com.iprism.elliot.domain.model.SuggestionModel
import com.iprism.elliot.domain.rules.Ruleset
import com.iprism.elliot.util.Resource
import com.iprism.elliot.util.Utils.getDateAndTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val repository: FoodRepository,
    private val sharedPref: SharedPreferences,
    private val ruleset: Ruleset
) : ViewModel() {

    private val ingredientsToIDs = mutableMapOf<String, Long>()

    var foodName = ""
    var date = ""
    var meal = ""
    var time = ""

    // Backing property to avoid state updates from other classes
    private val _ingredientsListUiState =
        MutableStateFlow(IngredientListUiState(emptyArray(), BooleanArray(0)))
    // val ingredientsListUiState = _ingredientsListUiState.asStateFlow()

    private val _oneTimeIngredientsListUiState = MutableSharedFlow<IngredientListUiState>()

    // The UI collects from this SharedFlow to get its state updates
    val oneTimeIngredientsListUiState = _oneTimeIngredientsListUiState.asSharedFlow()

    fun onEvent(event: CameraEvent) {
        when (event) {
            is CameraEvent.OnCameraButtonClick -> {
                val year = Calendar.getInstance().getDateAndTime(Calendar.YEAR)
                val month = Calendar.getInstance().getDateAndTime(Calendar.MONTH, isMonth = true)
                val day = Calendar.getInstance().getDateAndTime(Calendar.DAY_OF_MONTH)
                date = "$year-$month-$day"

                val hour = Calendar.getInstance().getDateAndTime(Calendar.HOUR_OF_DAY)
                val minutes = Calendar.getInstance().getDateAndTime(Calendar.MINUTE)
                val seconds = Calendar.getInstance().getDateAndTime(Calendar.SECOND)
                time = "$hour:$minutes:$seconds"

                with(sharedPref) {
                    meal = when {
                        hour.toInt() in getInt("breakfastStart", 8)..getInt("breakfastEnd", 9) -> {
                            "Breakfast"
                        }
                        hour.toInt() in getInt("lunchStart", 14)..getInt("lunchEnd", 15) -> {
                            "Lunch"
                        }
                        else -> {
                            "Dinner"
                        }
                    }
                }
            }
            is CameraEvent.OnConfirmationDialogOkClick -> {
                viewModelScope.launch {
                    insertFood()

                    val checked = _ingredientsListUiState.value.checked
                    val ingredients = _ingredientsListUiState.value.ingredients
                    for (i in 0 until checked?.size!!) {
                        if (checked[i]) repository.insertHistoryIngredients(
                            HistoryIngredientCrossRef(
                                historyId = repository.getLatestFoodHistoryId(),
                                ingredientId = ingredientsToIDs[ingredients?.get(i)]!!
                            )
                        )
                    }

//                    _ingredientsListUiState.value.checked?.zip(_ingredientsListUiState.value.ingredients) { checked, ingredient ->
//                        if (checked) repository.insertHistoryIngredients(
//                            HistoryIngredientCrossRef(
//                                historyId = repository.getLatestFoodHistoryId(),
//                                ingredientId = ingredientsToIDs[ingredient]!!
//                            )
//                        )
//                    }

                    val nutrients = repository.getLastSevenDaysNutrients()
                    val caloriesWeek = nutrients.carbohydrate + nutrients.fiber + nutrients.fat +
                            nutrients.protein + nutrients.sodium + nutrients.sugar

                    repository.getNutrients(date, date).collect {
                        val caloriesDay = it.carbohydrate + it.fiber + it.fat +
                                it.protein + it.sodium + it.sugar
                        for (rule in checkRuleset(nutrients, caloriesWeek, caloriesDay, it)) {
                            if (rule != "") {
                                repository.insertSuggestion(SuggestionModel(rule))
                            }
                        }
                    }

                }
            }
            is CameraEvent.OnMealEntryDialogOkClick -> {
                foodName = foodName.lowercase()
                getIngredients()
            }
            is CameraEvent.OnPredictionCheckDialogYesClick -> {
                foodName = event.foodName
                getIngredients()
            }
        }
    }

    suspend fun translateFoodNames(foodNames: Array<String>): Array<String> {
        val localeFoodNames = foodNames.map { name ->
            val id = repository.getFoodIdByName(name)
            repository.getFoodNameByIdAndLocale(id, Locale.getDefault().language)
        }.toTypedArray()

        return localeFoodNames
    }

    private suspend fun insertFood() {
        repository.insertFood(
            HistoryModel(
                food_name = foodName,
                date = date,
                meal = meal,
                time = time
            )
        )
    }

    private fun getIngredients() {
        viewModelScope.launch {
            repository.getFoodWithIngredients(foodName).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _ingredientsListUiState.value = _ingredientsListUiState.value.copy(
                            ingredients = resource.data?.map { it.ingredientName }?.toTypedArray(),
                            checked = resource.data?.map { it.ingredientName }
                                ?.toTypedArray()?.size?.let { BooleanArray(it) { true } }
                        )

                        resource.data?.forEach { ingredient ->
                            ingredientsToIDs[ingredient.ingredientName] = ingredient.ingredientId
                        }
                    }
                    is Resource.Error -> {
                        _ingredientsListUiState.value =
                            IngredientListUiState(error = IngredientListUiState.Error.NoSuchFood)
                    }
                }

                _oneTimeIngredientsListUiState.emit(_ingredientsListUiState.value)
            }
        }
    }

    private fun checkRuleset(
        nutrientsWeek: NutrientsModel,
        caloriesWeek: Float,
        caloriesDay: Float,
        nutrientsDay: NutrientsModel
    ): List<String> {
        val activeRules = mutableListOf<String>()
        val proteinPercentWeek = nutrientsWeek.protein / caloriesWeek
        val fatPercentWeek = nutrientsWeek.fat / caloriesWeek
        val carbsPercentWeek = nutrientsWeek.carbohydrate / caloriesWeek
        val sugarPercentWeek = nutrientsWeek.sugar / caloriesWeek


        val proteinPercentDay = nutrientsDay.protein / caloriesDay
        val fatPercentDay = nutrientsDay.fat / caloriesDay
        val carbsPercentDay = nutrientsDay.carbohydrate / caloriesDay
        val sugarPercentDay = nutrientsDay.sugar / caloriesDay

        activeRules.add(ruleset.proteinMinRuleWeek(proteinPercentWeek))
        activeRules.add(ruleset.proteinMaxRuleWeek(proteinPercentWeek))

        activeRules.add(ruleset.fatMinRuleWeek(fatPercentWeek))
        activeRules.add(ruleset.fatMaxRuleWeek(fatPercentWeek))
        activeRules.add(ruleset.saturatedFatRuleWeek(fatPercentWeek))

        activeRules.add(ruleset.carbsMinRuleWeek(carbsPercentWeek))
        activeRules.add(ruleset.carbsMaxRuleWeek(carbsPercentWeek))
        activeRules.add(ruleset.sugarRuleWeek(sugarPercentWeek))

        activeRules.add(ruleset.proteinMinRuleDay(proteinPercentDay))
        activeRules.add(ruleset.proteinMaxRuleDay(proteinPercentDay))

        activeRules.add(ruleset.fatMinRuleDay(fatPercentDay))
        activeRules.add(ruleset.fatMaxRuleDay(fatPercentDay))
        activeRules.add(ruleset.saturatedFatRuleDay(fatPercentDay))

        activeRules.add(ruleset.carbsMinRuleDay(carbsPercentDay))
        activeRules.add(ruleset.carbsMaxRuleDay(carbsPercentDay))

        activeRules.add(ruleset.fiberRuleDay(nutrientsDay.fiber))
        activeRules.add(ruleset.sodiumRule(nutrientsDay.sodium))
        activeRules.add(ruleset.sugarRuleDay(sugarPercentDay))

        activeRules.add(ruleset.futureSugarRule(sugarPercentWeek))
        activeRules.add(ruleset.futureFatRule(fatPercentWeek))
        activeRules.add(ruleset.futureSaturatedFatRule(fatPercentWeek))

        return activeRules
    }

    data class IngredientListUiState(
        val ingredients: Array<String>? = null,
        val checked: BooleanArray? = null,
        val isLoading: Boolean = false,
        val error: Error? = null
    ) {
        sealed class Error {
            object NoSuchFood : Error()
        }
    }
}