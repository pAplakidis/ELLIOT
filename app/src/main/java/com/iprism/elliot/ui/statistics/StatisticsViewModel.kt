package com.iprism.elliot.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.elliot.data.repository.FoodRepository
import com.iprism.elliot.domain.model.NutrientsModel
import com.iprism.elliot.util.Utils.getDateAndTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val repository: FoodRepository
) : ViewModel() {

    var dateStart = ""
    var dateEnd = ""

    init {
        val year = Calendar.getInstance().getDateAndTime(Calendar.YEAR)
        val month = Calendar.getInstance().getDateAndTime(Calendar.MONTH, isMonth = true)
        val day = Calendar.getInstance().getDateAndTime(Calendar.DAY_OF_MONTH)

        dateStart = "$year-$month-$day"
        dateEnd = "$year-$month-$day"
    }

    private val _dateState = MutableStateFlow(DateChooser(emptyList()))
    val dateState = _dateState.asStateFlow()

    private val _statState =
        MutableStateFlow(StatLoader(NutrientsModel(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)))
    val statState = _statState.asStateFlow()

    fun onEvent(event: StatisticsEvent) {
        when (event) {
            is StatisticsEvent.OnDateChoose -> {
                viewModelScope.launch {
                    repository.getNutrients(dateStart, dateEnd).collect { nutrients ->
                        val protein = nutrients.protein
                        val fat = nutrients.fat
                        val carbs = nutrients.carbohydrate
                        val fiber = nutrients.fiber
                        val sodium = nutrients.sodium
                        val sugar = nutrients.sugar

                        val total = protein + carbs + fat + fiber + sodium + sugar

                        val proteinPerc = (protein / total * 100)
                        val fatPerc = (fat / total * 100)
                        val carbsPerc = (carbs / total * 100)
                        val fiberPerc = (fiber / total * 100)
                        val sodiumPerc = (sodium / total * 100)
                        val sugarPerc = (sugar / total * 100)

                        _dateState.value = dateState.value.copy(
                            pieValues = listOf(
                                proteinPerc,
                                fatPerc,
                                carbsPerc,
                                fiberPerc,
                                sodiumPerc,
                                sugarPerc
                            )
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