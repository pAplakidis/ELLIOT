package com.iprism.elliot.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.elliot.data.repository.FoodRepository
import com.iprism.elliot.domain.model.Statistic
import com.iprism.elliot.domain.model.SubStatistic
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val repository: FoodRepository
) : ViewModel() {

    private val testListBreakfast = Statistic(
        listOf(
            SubStatistic("Protein", "653"),
            SubStatistic("Carbs", "23"),
            SubStatistic("Fat", "24")
        )
    )

    private val testListLunch = Statistic(
        listOf(
            SubStatistic("Protein", "356"),
            SubStatistic("Carbs", "12"),
            SubStatistic("Fat", "35")
        )
    )

    private val testListDinner = Statistic(
        listOf(
            SubStatistic("Protein", "62"),
            SubStatistic("Carbs", "235"),
            SubStatistic("Fat", "13")
        )
    )

    private val _dateState = MutableStateFlow(DateChooser(emptyList()))
    val dateState: StateFlow<DateChooser> = _dateState

    private val _statState = MutableStateFlow(StatLoader(Statistic(emptyList())))
    val statState: StateFlow<StatLoader> = _statState

    fun onEvent(event: StatisticsEvent) {
        when (event) {
            is StatisticsEvent.OnDateChoose -> {
                viewModelScope.launch {
                    // EDW THA PAIRNONTAI ME VASH TIS HMEROMHNIES
                    val protein = 300f
                    val carbs = 200f
                    val fat = 100f
                    val total = protein + carbs + fat

                    val proteinPerc = (protein / total * 100)
                    val carbsPerc = (carbs / total * 100)
                    val fatPerc = (fat / total * 100)

                    _dateState.value = dateState.value.copy(
                        pieValues = listOf(proteinPerc, carbsPerc, fatPerc)
                    )
                }
            }
            is StatisticsEvent.OnStatLoad -> {
                viewModelScope.launch {
                    // EDW THA FORTWNONTAI ME VASH TO MEAL
                    when (event.mealName) {
                        "Breakfast" -> {
                            _statState.value = statState.value.copy(
                                statList = testListBreakfast
                            )
                        }
                        "Lunch" -> {
                            _statState.value = statState.value.copy(
                                statList = testListLunch
                            )
                        }
                        else -> {
                            _statState.value = statState.value.copy(
                                statList = testListDinner
                            )
                        }
                    }
                }
            }
        }
    }

    data class StatLoader(val statList: Statistic)
    data class DateChooser(val pieValues: List<Float>)
}