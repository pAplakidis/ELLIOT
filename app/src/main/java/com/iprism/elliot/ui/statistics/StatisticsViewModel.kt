package com.iprism.elliot.ui.statistics

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.elliot.data.repository.FoodRepository
import com.iprism.elliot.domain.model.NutrientsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    application: Application,
    private val repository: FoodRepository
) : AndroidViewModel(application) {

    var dateStart = ""
    var dateEnd = ""

    private val _dateState = MutableStateFlow(DateChooser(emptyList()))
    val dateState = _dateState.asStateFlow()

    private val _statState = MutableStateFlow(StatLoader(NutrientsModel(0.0,0.0,0.0,0.0,0.0, 0.0)))
    val statState = _statState.asStateFlow()

    fun onEvent(event: StatisticsEvent) {
        when (event) {
            is StatisticsEvent.OnDateChoose -> {
                viewModelScope.launch {
                    repository.getNutrients(dateStart, dateEnd).collect { nutrients ->
                        val protein = nutrients.protein.toFloat()
                        val fat = nutrients.fat.toFloat()
                        val carbs = nutrients.carbohydrate.toFloat()
                        val fiber = nutrients.fiber.toFloat()
                        val sodium = nutrients.sodium.toFloat()
                        //val sugar = nutrients.sugar.toFloat()

                        val total = protein + carbs + fat + fiber + sodium

                        val proteinPerc = (protein / total * 100)
                        val fatPerc = (fat / total * 100)
                        val carbsPerc = (carbs / total * 100)
                        val fiberPerc = (fiber / total * 100)
                        val sodiumPerc = (sodium / total * 100)
                        //val sugarPerc = (sugar / total * 100)

                        _dateState.value = dateState.value.copy(
                            pieValues = listOf(proteinPerc, fatPerc, carbsPerc, fiberPerc, sodiumPerc) // SUGARPERC
                        )
                    }
                }
            }
            is StatisticsEvent.OnStatLoad -> {
                viewModelScope.launch {
                    repository.getNutrientsByMeal(dateStart, dateEnd, event.mealName)
                        .collect { nutrients ->
                             _statState.value = statState.value.copy(statList = nutrients)
                        }
                }
            }
        }
    }

    data class StatLoader(val statList: NutrientsModel)
    data class DateChooser(val pieValues: List<Float>)
}