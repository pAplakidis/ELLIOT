package com.iprism.elliot.ui.statistics

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.elliot.R
import com.iprism.elliot.data.repository.FoodRepository
import com.iprism.elliot.domain.model.Statistic
import com.iprism.elliot.domain.model.SubStatistic
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

    private val testListBreakfast = Statistic(
        listOf(
            SubStatistic(
                getApplication<Application>().resources.getString(R.string.proteins),
                "653"
            ),
            SubStatistic(getApplication<Application>().resources.getString(R.string.carbs), "23"),
            SubStatistic(getApplication<Application>().resources.getString(R.string.fat), "24")
        )
    )

    private val testListLunch = Statistic(
        listOf(
            SubStatistic(
                getApplication<Application>().resources.getString(R.string.proteins),
                "356"
            ),
            SubStatistic(getApplication<Application>().resources.getString(R.string.carbs), "12"),
            SubStatistic(getApplication<Application>().resources.getString(R.string.fat), "35")
        )
    )

    private val testListDinner = Statistic(
        listOf(
            SubStatistic(
                getApplication<Application>().resources.getString(R.string.proteins),
                "62"
            ),
            SubStatistic(getApplication<Application>().resources.getString(R.string.carbs), "235"),
            SubStatistic(getApplication<Application>().resources.getString(R.string.fat), "13")
        )
    )

    private val _dateState = MutableStateFlow(DateChooser(emptyList()))
    val dateState = _dateState.asStateFlow()

    private val _statState = MutableStateFlow(StatLoader(Statistic(emptyList())))
    val statState = _statState.asStateFlow()

    fun onEvent(event: StatisticsEvent) {
        when (event) {
            is StatisticsEvent.OnDateChoose -> {
                viewModelScope.launch {
                    repository.getNutrients(dateStart, dateEnd).collect { nutrients ->
                        val protein = nutrients.protein.toFloat()
                        val carbs = nutrients.carbohydrate.toFloat()
                        val fat = nutrients.fat.toFloat()
                        val total = protein + carbs + fat

                        val proteinPerc = (protein / total * 100)
                        val carbsPerc = (carbs / total * 100)
                        val fatPerc = (fat / total * 100)

                        _dateState.value = dateState.value.copy(
                            pieValues = listOf(proteinPerc, carbsPerc, fatPerc)
                        )
                    }
                }
            }
            is StatisticsEvent.OnStatLoad -> {
                viewModelScope.launch {
                    repository.getNutrientsByMeal(dateStart, dateEnd, event.mealName)
                        .collect { nutrients ->
                            // _statState.value = statState.value.copy(statList =)
                        }
                }
            }
        }
    }

    data class StatLoader(val statList: Statistic)
    data class DateChooser(val pieValues: List<Float>)
}